package data_access;

import entity.Article;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.filter_news.FilterNewsUserDataAccessInterface;
import use_case.search_news.SearchNewsUserDataAccessInterface;
import use_case.top_headlines.TopHeadlinesUserDataAccessInterface;
import use_case.discover_page.DiscoverPageDataAccessInterface;

import java.io.*;
import java.util.*;

public class DBUserDataAccessObject implements
        TopHeadlinesUserDataAccessInterface,
        SearchNewsUserDataAccessInterface,
        FilterNewsUserDataAccessInterface,
        DiscoverPageDataAccessInterface {

    private static final String API_KEY = "pub_24c036e0a4b64b0a82914e3fabe9e090";

    private static final String TOP_URL =
            "https://newsdata.io/api/1/news?country=us&language=en&category=top&removeduplicate=1&apikey=" + API_KEY;

    private static final String SEARCH_URL =
            "https://newsdata.io/api/1/news?language=en&removeduplicate=1&apikey=" + API_KEY + "&q=";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<Article> fetchTopHeadlines() {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            String nextPage = null;

            while (articles.size() < 50) {
                String url = TOP_URL;
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
                    extractArticles(results, articles, seen);

                    // Stop early if we hit 50
                    if (articles.size() >= 50) break;
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
    public List<Article> searchByKeyword(String keyword) {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            String nextPage = null;

            while (articles.size() < 1000) {
                String url = SEARCH_URL + keyword;

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
                    extractArticles(results, articles, seen);

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
                                 Set<String> seen) {

        for (int i = 0; i < results.length(); i++) {
            JSONObject a = results.getJSONObject(i);

            String title = a.optString("title", "").trim();
            String source = a.optString("source_id", "Unknown").trim();

            String key = title.toLowerCase() + "|" + source.toLowerCase();
            if (title.isEmpty() || seen.contains(key)) continue;
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

    /**
     * Fetches articles for one or more topics (Use Case 10).
     * Makes an API call for each topic and merges the results.
     *
     * @param topics the topics to filter by (e.g. "sports", "business")
     * @return a list of articles that match any of the topics
     */
    @Override
    public List<Article> filterByTopics(List<String> topics) {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        if (topics == null || topics.isEmpty()) {
            return articles;
        }

        for (String topic : topics) {
            if (topic == null || topic.isBlank()) {
                continue;
            }

            try {
                String url = "https://newsdata.io/api/1/news?"
                        + "category=" + topic
                        + "&language=en"
                        + "&removeduplicate=1"
                        + "&apikey=" + API_KEY;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    System.err.println("API Error (filterByTopics): HTTP " + response.code());
                    continue;
                }

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                JSONArray results = obj.optJSONArray("results");

                if (results == null) {
                    continue;
                }

                for (int i = 0; i < results.length(); i++) {
                    JSONObject a = results.getJSONObject(i);

                    String title = a.optString("title", "").trim();
                    String source = a.optString("source_id", "Unknown").trim();
                    String key = (title + "|" + source).toLowerCase();

                    // Skip empty titles and duplicates across topics
                    if (title.isEmpty() || seen.contains(key)) {
                        continue;
                    }
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
            } catch (Exception e) {
                System.err.println("Error filtering news by topic: " + e.getMessage());
            }
        }

        return articles;
    }

    /**
     * Returns the user's reading history (saved articles) for the Discover page.
     */
    @Override
    public List<Article> getReadingHistory() {
        List<Article> savedArticles = new ArrayList<>();
        // Use the same path as FileSaveArticleDataAccess for consistency
        File savedFile = new File("data/saved_articles.txt");

        // Resolve to absolute path to avoid confusion
        String absolutePath = savedFile.getAbsolutePath();
        savedFile = new File(absolutePath);

        if (!savedFile.exists()) {
            return savedArticles;
        }

        // Read the file fresh each time - no caching
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

    /**
     * Picks the top-N most frequent words from article titles (ignoring stop words).
     */
    @Override
    public Set<String> extractTopTopics(List<Article> articles, int topN) {
        if (articles == null || articles.isEmpty() || topN <= 0) {
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
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCount.entrySet());

        entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
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

    /**
     * Searches for articles related to a set of topics, with simple pagination.
     *
     * @param topics set of topic keywords
     * @param page   zero-based page index (0 = first "page" of results)
     * @return up to 10 articles for the requested page
     */
    @Override
    public List<Article> searchByTopics(Set<String> topics, int page) {
        if (topics == null || topics.isEmpty() || page < 0) {
            return new ArrayList<>();
        }

        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            List<String> topicList = new ArrayList<>(topics);
            StringBuilder queryBuilder = new StringBuilder();
            int maxTopics = Math.min(3, topicList.size());

            for (int i = 0; i < maxTopics; i++) {
                if (i > 0) {
                    queryBuilder.append(" OR ");
                }
                queryBuilder.append(topicList.get(i));
            }

            String query = queryBuilder.toString();

            String nextPage = null;
            int currentPageIndex = 0;

            // Navigate to the requested page
            while (currentPageIndex <= page) {
                String url = "https://newsdata.io/api/1/news?"
                        + "q=" + java.net.URLEncoder.encode(query, "UTF-8")
                        + "&language=en"
                        + "&removeduplicate=1"
                        + "&apikey=" + API_KEY;

                if (nextPage != null) {
                    url += "&page=" + nextPage;
                }

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    System.err.println("API Error (searchByTopics): HTTP " + response.code());
                    break;
                }

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                JSONArray results = obj.optJSONArray("results");

                if (results == null) {
                    break;
                }

                // Only extract articles if we're on the requested page
                // Limit to 10 articles per page to reduce API calls
                if (currentPageIndex == page) {
                    int maxArticles = 10;
                    for (int i = 0; i < results.length() && articles.size() < maxArticles; i++) {
                        JSONObject a = results.getJSONObject(i);

                        String title = a.optString("title", "").trim();
                        String source = a.optString("source_id", "Unknown").trim();
                        String key = title.toLowerCase() + "|" + source.toLowerCase();

                        if (title.isEmpty() || seen.contains(key)) continue;
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
                    // No more pages available
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
