package data_access.generate_credibility;

import entity.Article;
import entity.CredibilityScore;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.generate_credibility.GenerateCredibilityDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class GenerateCredibilityAPIsDataAccessObject implements GenerateCredibilityDataAccessInterface {

    private static final String TEXTRAZOR_API_KEY =
            getEnvOrThrow("TEXTRAZOR_API_KEY", "TextRazor API key");
    private static final String TEXTRAZOR_ENDPOINT = "https://api.textrazor.com/";

    private static final String OPR_API_KEY =
            getEnvOrThrow("OPENPAGERANK_API_KEY", "OpenPageRank API key");
    private static final String OPR_ENDPOINT = "https://openpagerank.com/api/v1.0/getPageRank";

    private static String getEnvOrThrow(String envName, String label) {
        String value = System.getenv(envName);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    label + " not set. Please define environment variable: " + envName);
        }
        return value;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public Article enrichArticleContent(Article article) {
        if (article.getContent() == null || article.getContent().isBlank()) {
            StringBuilder sb = new StringBuilder();
            if (article.getTitle() != null) {
                sb.append(article.getTitle()).append(". ");
            }
            if (article.getDescription() != null) {
                sb.append(article.getDescription());
            }
            article.setContent(sb.toString());
        }
        return article;
    }

    @Override
    public CredibilityScore generateScore(Article article) throws Exception {
        Article enriched = enrichArticleContent(article);
        String text = enriched.getContent();
        if (text == null || text.isBlank()) {
            System.out.println("[Credibility] No text content, returning null score.");
            return null; // insufficient data
        }

        System.out.println("[Credibility] Calling TextRazor + OpenPageRank for article:");
        System.out.println("    title  = " + enriched.getTitle());
        System.out.println("    url    = " + enriched.getUrl());
        System.out.println("    source = " + enriched.getSource());

        TextSignals textSignals;
        try {
            textSignals = fetchTextSignals(text);
        } catch (Exception e) {
            System.out.println("[Credibility] TextRazor failed: " + e.getMessage());
            System.out.println("[Credibility] Using fallback text signals (0.5, 0.5).");
            textSignals = new TextSignals(0.5, 0.5);
        }

        double sourceScore = fetchSourceScoreFromOpenPageRank(article.getUrl());

        System.out.println("[Credibility] TextRazor: sentimentScore=" + textSignals.sentimentScore +
                ", claimConfidence=" + textSignals.claimConfidence);
        System.out.println("[Credibility] OpenPageRank: sourceScore=" + sourceScore);

        CredibilityScore score = new CredibilityScore(
                sourceScore,
                textSignals.sentimentScore,
                textSignals.claimConfidence
        );

        System.out.println("[Credibility] Combined overallTrust=" + score.getOverallTrust() +
                ", level=" + score.getLevel());
        return score;
    }


    private static class TextSignals {
        final double sentimentScore;
        final double claimConfidence;
        TextSignals(double sentimentScore, double claimConfidence) {
            this.sentimentScore = sentimentScore;
            this.claimConfidence = claimConfidence;
        }
    }

    private TextSignals fetchTextSignals(String text) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("text", text)
                .add("extractors", "entities,sentiment")
                .build();

        Request request = new Request.Builder()
                .url(TEXTRAZOR_ENDPOINT)
                .post(formBody)
                .addHeader("x-textrazor-key", TEXTRAZOR_API_KEY)
                .build();

        System.out.println("[TextRazor] Sending request...");
        try (Response response = client.newCall(request).execute()) {
            System.out.println("[TextRazor] HTTP status: " + response.code());

            String json = response.body().string();
            System.out.println("[TextRazor] Raw JSON (first 500 chars):");
            System.out.println(json.length() > 500 ? json.substring(0, 500) + "..." : json);

            if (!response.isSuccessful()) {
                throw new IOException("TextRazor error: HTTP " + response.code());
            }

            JSONObject root = new JSONObject(json);
            JSONObject resp = root.optJSONObject("response");
            if (resp == null) {
                System.out.println("[TextRazor] No 'response' object in JSON. Using defaults.");
                return new TextSignals(0.5, 0.5);
            }

            double polarity = 0.0;
            JSONObject sentiment = resp.optJSONObject("sentiment");
            if (sentiment != null) {
                polarity = sentiment.optDouble("score", 0.0);
            }
            double sentimentScore = (polarity + 1.0) / 2.0; // map [-1,1] -> [0,1]

            JSONArray entities = resp.optJSONArray("entities");
            double avgConfidence = 0.0;
            if (entities != null && entities.length() > 0) {
                double sum = 0.0;
                for (int i = 0; i < entities.length(); i++) {
                    JSONObject ent = entities.optJSONObject(i);
                    if (ent != null) {
                        sum += ent.optDouble("confidenceScore", 0.0);
                    }
                }
                avgConfidence = sum / entities.length();
            }

            double scaledConf = Math.tanh(avgConfidence / 5.0);
            if (scaledConf < 0.0) scaledConf = 0.0;
            if (scaledConf > 1.0) scaledConf = 1.0;
            double claimConfidence = scaledConf;

            System.out.printf(
                    "[TextRazor] polarity=%.3f -> sentimentScore=%.3f, avgEntityConf=%.3f -> claimConfidence=%.3f%n",
                    polarity, sentimentScore, avgConfidence, claimConfidence
            );

            return new TextSignals(sentimentScore, claimConfidence);
        }
    }

    private double fetchSourceScoreFromOpenPageRank(String articleUrl) throws IOException {
        if (articleUrl == null || articleUrl.isBlank()) {
            System.out.println("[OpenPageRank] No URL, returning neutral 0.5");
            return 0.5;
        }
        String domain = extractDomain(articleUrl);
        if (domain == null) {
            System.out.println("[OpenPageRank] Could not extract domain, returning neutral 0.5");
            return 0.5;
        }

        System.out.println("[OpenPageRank] Domain = " + domain);

        HttpUrl url = HttpUrl.parse(OPR_ENDPOINT).newBuilder()
                .addQueryParameter("domains[]", domain)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("API-OPR", OPR_API_KEY)
                .build();

        System.out.println("[OpenPageRank] Request URL: " + url);

        try (Response response = client.newCall(request).execute()) {
            System.out.println("[OpenPageRank] HTTP status: " + response.code());

            String json = response.body().string();
            System.out.println("[OpenPageRank] Raw JSON (first 500 chars):");
            System.out.println(json.length() > 500 ? json.substring(0, 500) + "..." : json);

            if (!response.isSuccessful()) {
                throw new IOException("OpenPageRank error: HTTP " + response.code());
            }

            JSONObject root = new JSONObject(json);
            JSONArray results = root.optJSONArray("response");
            if (results == null || results.length() == 0) {
                System.out.println("[OpenPageRank] No 'response' array, returning neutral 0.5");
                return 0.5;
            }

            JSONObject first = results.getJSONObject(0);
            double rankDecimal = first.optDouble("page_rank_decimal", 5.0);
            double normalized = rankDecimal / 10.0;
            if (normalized < 0.0) normalized = 0.0;
            if (normalized > 1.0) normalized = 1.0;

            System.out.println("[OpenPageRank] page_rank_decimal=" + rankDecimal +
                    ", normalized=" + normalized);

            return normalized;
        }
    }

    private String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null) return null;
            return host.startsWith("www.") ? host.substring(4) : host;
        } catch (URISyntaxException e) {
            System.out.println("[OpenPageRank] URISyntaxException for url=" + url + ": " + e.getMessage());
            return null;
        }
    }
}
