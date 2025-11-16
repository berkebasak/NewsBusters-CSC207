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

import java.util.*;

public class DBUserDataAccessObject implements
        TopHeadlinesUserDataAccessInterface,
        SearchNewsUserDataAccessInterface,
        FilterNewsUserDataAccessInterface {

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
                if (nextPage != null) url += "&page=" + nextPage;

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

            while (articles.size() < 10000) {
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
                    System.err.println("API Error (filter): HTTP " + response.code());
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
}
