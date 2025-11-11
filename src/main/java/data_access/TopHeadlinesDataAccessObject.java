package data_access;

import entity.Article;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.top_headlines.TopHeadlinesUserDataAccessInterface;

import java.util.*;

public class TopHeadlinesDataAccessObject implements TopHeadlinesUserDataAccessInterface {
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
}
