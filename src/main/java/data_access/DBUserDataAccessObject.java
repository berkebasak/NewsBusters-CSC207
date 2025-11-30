package data_access;

import entity.Article;
import entity.UserPreferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search_news.SearchNewsUserDataAccessInterface;
import use_case.top_headlines.TopHeadlinesUserDataAccessInterface;
import use_case.discover_page.DiscoverPageDataAccessInterface;

import java.io.*;
import java.util.*;

public class DBUserDataAccessObject implements
        TopHeadlinesUserDataAccessInterface,
        SearchNewsUserDataAccessInterface,
        DiscoverPageDataAccessInterface {

    private static final String API_KEY = getEnvOrThrow("NEWSDATA_API_KEY", "NewsData API key");

    private static String getEnvOrThrow(String envName, String label) {
        String value = System.getenv(envName);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    label + " not set. Please define environment variable: " + envName);
        }
        return value;
    }
    private static final String TOP_URL =
            "https://newsdata.io/api/1/news?country=us&language=en&category=top&removeduplicate=1&apikey=" + API_KEY;

    private static final String SEARCH_URL =
            "https://newsdata.io/api/1/news?country=us&language=en&removeduplicate=1&apikey=" + API_KEY + "&q=";

    private final OkHttpClient client = new OkHttpClient();

    private String applyPreferredTopics(String url, UserPreferences userPreferences) {
        String query = "";
        ArrayList<String> preferredTopics = userPreferences.getPreferredTopics();

        for (int i = 0; i < preferredTopics.size(); i++) {
            if (i > 0)
                query += " OR ";
            query += preferredTopics.get(i);
        }

        try {
            String replacement = "https://newsdata.io/api/1/news?"
                    + "q=" + java.net.URLEncoder.encode(query, "UTF-8") + "&";
            return url.replace("https://newsdata.io/api/1/news?", replacement);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error Applying Preferred Topics: " + e.getMessage());
        }
        return url;
    }

    private String applyLanguageAndCountry(String url, UserPreferences userPreferences) {
        String language = "language=" + userPreferences.getLanguage();
        String country = "country=" + userPreferences.getRegion();
        return url.replace("language=en", language).replace("country=us", country);
    }

    private boolean isSourceBlocked(String source, UserPreferences userPreferences) {
        return userPreferences.getBlockedSources().contains(source);
    }

    @Override
    public List<Article> fetchTopHeadlines(UserPreferences userPreferences) {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        String customizedURL = applyLanguageAndCountry(TOP_URL, userPreferences);
        customizedURL = applyPreferredTopics(customizedURL, userPreferences);

        try {
            String nextPage = null;

            while (articles.size() < 20) {
                String url = customizedURL;
                if (nextPage != null) url += "&page=" + nextPage;

                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) break;

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);

                JSONArray results = obj.optJSONArray("results");
                if (results != null) {
                    extractArticles(results, articles, seen, userPreferences);

                    if (articles.size() >= 20) break;
                }

                nextPage = obj.optString("nextPage", null);
                if (nextPage == null || nextPage.isEmpty()) break;
            }

        } catch (Exception e) {
            System.err.println("Error fetching headlines: " + e.getMessage());
        }

        return articles.subList(0, Math.min(articles.size(), 50));
    }

    @Override
    public List<Article> searchByKeyword(String keyword, UserPreferences userPreferences) {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        String customizedSearchURL = applyLanguageAndCountry(SEARCH_URL, userPreferences);

        try {
            String nextPage = null;

            while (articles.size() < 1000) {
                String url = customizedSearchURL + keyword;

                if (nextPage != null) {
                    url += "&page=" + nextPage;
                }

                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) break;

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);

                JSONArray results = obj.optJSONArray("results");
                if (results != null) {
                    extractArticles(results, articles, seen, userPreferences);

                    if (articles.size() >= 1000) break;
                }

                nextPage = obj.optString("nextPage", null);
                if (nextPage == null || nextPage.isEmpty()) break;
            }

        } catch (Exception e) {
            System.err.println("Error searching news: " + e.getMessage());
        }

        return articles.subList(0, Math.min(articles.size(), 1000));
    }


    private void extractArticles(JSONArray results,
                                 List<Article> articles,
                                 Set<String> seen,
                                 UserPreferences userPreferences) {

        for (int i = 0; i < results.length(); i++) {
            JSONObject a = results.getJSONObject(i);

            String title = a.optString("title", "").trim();
            String source = a.optString("source_id", "Unknown").trim();

            String key = title.toLowerCase() + "|" + source.toLowerCase();
            if (title.isEmpty() || seen.contains(key) || isSourceBlocked(source, userPreferences)) continue;
            seen.add(key);

            articles.add(new Article(
                    UUID.randomUUID().toString(),
                    title,
                    a.optString("description", ""),
                    a.optString("link", ""),
                    a.optString("image_url", ""),
                    source
            ));
        }
    }

    @Override
    public List<Article> getReadingHistory() {
        List<Article> savedArticles = new ArrayList<>();
        // Use the same path as FileSaveArticleDataAccess for consistency
        // Create file using the same relative path resolution
        File savedFile = new File("data/saved_articles.txt");
        
        // Get absolute path to ensure we're reading from the correct location
        String absolutePath = savedFile.getAbsolutePath();
        savedFile = new File(absolutePath);

        if (!savedFile.exists()) {
            return savedArticles;
        }


        try (FileReader fr = new FileReader(savedFile);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|", 3);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String url = parts[2].trim();

                    if (!title.isEmpty() && !url.isEmpty()) {
                        Article article = new Article(id, title, "", url, "", "Unknown");
                        savedArticles.add(article);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading saved articles: " + e.getMessage());
        }

        return savedArticles;
    }

    @Override
    public Set<String> extractTopTopics(List<Article> articles, int topN) {
        if (articles == null || articles.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> stopWords = new HashSet<>(Arrays.asList(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
            "of", "with", "by", "from", "as", "is", "are", "was", "were", "be",
            "been", "being", "have", "has", "had", "do", "does", "did", "will",
            "would", "should", "could", "may", "might", "must", "can", "this",
            "that", "these", "those", "i", "you", "he", "she", "it", "we", "they",
            "what", "which", "who", "when", "where", "why", "how", "all", "each",
            "every", "both", "few", "more", "most", "other", "some", "such", "no",
            "nor", "not", "only", "own", "same", "so", "than", "too", "very"
        ));

        Map<String, Integer> wordCount = new HashMap<>();

        for (Article article : articles) {
            String title = article.getTitle();
            if (title == null || title.isEmpty()) continue;

            String[] words = title.toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", " ")
                    .split("\\s+");

            for (String word : words) {
                word = word.trim();
                if (word.length() >= 3 && !stopWords.contains(word)) {
                    if (wordCount.containsKey(word)) {
                        wordCount.put(word, Integer.valueOf(wordCount.get(word) + 1));
                    } else {
                        wordCount.put(word, Integer.valueOf(1));
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCount.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Set<String> topTopics = new HashSet<>();
        int count = Math.min(topN, entryList.size());
        for (int i = 0; i < count; i++) {
            topTopics.add(entryList.get(i).getKey());
        }

        return topTopics;
    }

    @Override
    public List<Article> searchByTopics(Set<String> topics, int page, UserPreferences userPreferences) {
        if (topics == null || topics.isEmpty()) {
            return new ArrayList<>();
        }

        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            List<String> topicList = new ArrayList<>(topics);
            String query = "";
            int maxTopics = Math.min(3, topicList.size());

            for (int i = 0; i < maxTopics; i++) {
                if (i > 0) {
                    query += " OR ";
                }
                query += topicList.get(i);
            }

            String nextPage = null;
            int currentPageIndex = 0;
            
            // Navigate to the requested page
            while (currentPageIndex <= page) {
                String url = "https://newsdata.io/api/1/news?"
                        + "q=" + java.net.URLEncoder.encode(query, "UTF-8")
                        + "&country=us"
                        + "&language=en"
                        + "&removeduplicate=1"
                        + "&apikey=" + API_KEY;
                url = applyLanguageAndCountry(url, userPreferences);

                if (nextPage != null) {
                    url += "&page=" + nextPage;
                }

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    System.err.println("API Error: HTTP " + response.code());
                    break;
                }

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                JSONArray results = obj.optJSONArray("results");
                
                if (results == null) break;
                
                // Only extract articles if we're on the requested page
                // Limit to 10 articles per page to reduce API calls
                if (currentPageIndex == page) {
                    int maxArticles = 10;
                    for (int i = 0; i < results.length() && articles.size() < maxArticles; i++) {
                        JSONObject a = results.getJSONObject(i);

                        String title = a.optString("title", "").trim();
                        String source = a.optString("source_id", "Unknown").trim();
                        String key = title.toLowerCase() + "|" + source.toLowerCase();

                        if (title.isEmpty() || seen.contains(key) || isSourceBlocked(source, userPreferences)) continue;
                        seen.add(key);

                        articles.add(new Article(
                                UUID.randomUUID().toString(),
                                title,
                                a.optString("description", ""),
                                a.optString("link", ""),
                                a.optString("image_url", ""),
                                source
                        ));
                    }
                }
                
                nextPage = obj.optString("nextPage", null);
                if (nextPage == null || nextPage.isEmpty()) {
                    break;
                }
                
                currentPageIndex++;
            }
        } catch (Exception e) {
            System.err.println("Error searching by topics: " + e.getMessage());
        }

        return articles;
    }

}
