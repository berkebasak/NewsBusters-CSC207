package data_access;

import entity.Article;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search_news.SearchNewsInteractor;
import use_case.search_news.SearchNewsUserDataAccessInterface;
import use_case.top_headlines.TopHeadlinesUserDataAccessInterface;
import use_case.discover_page.DiscoverPageDataAccessInterface;

import java.io.*;
import java.util.*;

public class DBUserDataAccessObject implements TopHeadlinesUserDataAccessInterface, SearchNewsUserDataAccessInterface, DiscoverPageDataAccessInterface {
    private static final String API_KEY = "pub_bfcfbe16d3df4bf4b577b1b6096daf57";
    private static final String BASE_URL = "https://newsdata.io/api/1/news?country=us&language=en&category=top&removeduplicate=1";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<Article> fetchTopHeadlines() {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "&apikey=" + API_KEY)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("API Error: HTTP " + response.code());
                return articles;
            }

            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            JSONArray results = obj.optJSONArray("results");
            if (results == null) return articles;

            for (int i = 0; i < results.length(); i++) {
                JSONObject a = results.getJSONObject(i);
                String title = a.optString("title", "").trim();
                String source = a.optString("source_id", "Unknown").trim();
                String key = title.toLowerCase() + "|" + source.toLowerCase();

                // Skip duplicates
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
        } catch (Exception e) {
            System.err.println("Error fetching headlines: " + e.getMessage());
        }
        return articles;
    }

    /**
     * Search by Keyword (Use Case 9)
     */
    @Override
    public List<Article> searchByKeyword(String keyword) {
        List<Article> articles = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try {
            String url = "https://newsdata.io/api/1/news?"
                    + "q=" + keyword
                    + "&language=en"
                    + "&removeduplicate=1"
                    + "&apikey=" + API_KEY;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("API Error: HTTP " + response.code());
                return articles;
            }

            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            JSONArray results = obj.optJSONArray("results");
            if (results == null) return articles;

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
        } catch (Exception e) {
            System.err.println("Error searching news: " + e.getMessage());
        }

        return articles;
    }


    @Override
    public List<Article> getReadingHistory() {
        List<Article> savedArticles = new ArrayList<>();
        File savedFile = new File("data/saved_articles.txt");
        
        if (!savedFile.exists()) {
            return savedArticles;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(savedFile))) {
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
            
            // Split title into words, convert to lowercase, remove punctuation
            String[] words = title.toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", " ")
                    .split("\\s+");
            
            for (String word : words) {
                word = word.trim();
                // Skip stop words and short words (less than 3 characters)
                if (word.length() >= 3 && !stopWords.contains(word)) {
                    if (wordCount.containsKey(word)) {
                        wordCount.put(word, wordCount.get(word) + 1);
                    } else {
                        wordCount.put(word, 1);
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCount.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue()); // Descending order
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
    public List<Article> searchByTopics(Set<String> topics) {
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
            
            String url = "https://newsdata.io/api/1/news?"
                    + "q=" + java.net.URLEncoder.encode(query, "UTF-8")
                    + "&language=en"
                    + "&removeduplicate=1"
                    + "&apikey=" + API_KEY;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("API Error: HTTP " + response.code());
                return articles;
            }

            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            JSONArray results = obj.optJSONArray("results");
            if (results == null) return articles;

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
        } catch (Exception e) {
            System.err.println("Error searching by topics: " + e.getMessage());
        }

        return articles;
    }

}
