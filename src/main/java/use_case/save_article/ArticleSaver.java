package use_case.save_article;

import entity.Article;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleSaver {

    private final List<Article> savedArticles = new ArrayList<>();
    private static final String FILE_PATH = "saved_articles.txt"; // local file path

    public ArticleSaver() {
        loadFromFile();
    }

    public String save(Article article) {
        if (article == null || article.getUrl() == null) {
            return "Could not save.";
        }

        // Check for duplicates (by URL)
        for (Article a : savedArticles) {
            if (article.getUrl().equals(a.getUrl())) {
                return "Already saved.";
            }
        }

        // Add to memory
        savedArticles.add(article);
        article.setSaved(true);

        // Try to persist to file
        boolean success = appendToFile(article);
        if (!success) {
            // Roll back if file write fails
            savedArticles.removeLast();
            article.setSaved(false);
            return "Could not save.";
        }

        return "Saved.";
    }

    /** Returns all saved articles currently in memory */
    public List<Article> getSavedArticles() {
        return new ArrayList<>(savedArticles);
    }

    // ================== Helper methods below ==================

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return; // nothing saved yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Each line = id, title, description, url, imageUrl, source (tab-separated)
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", -1); // -1 keeps empty fields
                if (parts.length >= 6) {
                    String id          = parts[0];
                    String title       = parts[1];
                    String description = parts[2];
                    String url         = parts[3];
                    String imageUrl    = parts[4];
                    String source      = parts[5];

                    Article loadedArticle = new Article(id, title, description, url, imageUrl, source);
                    loadedArticle.setSaved(true);
                    savedArticles.add(loadedArticle);
                }
            }
        } catch (IOException e) {
             e.printStackTrace();
        }
    }

    private boolean appendToFile(Article article) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String id          = safe(article.getId());
            String title       = safe(article.getTitle());
            String description = safe(article.getDescription());
            String url         = safe(article.getUrl());
            String imageUrl    = safe(article.getImageUrl());
            String source      = safe(article.getSource());

            writer.write(id + "\t" + title + "\t" + description + "\t"
                    + url + "\t" + imageUrl + "\t" + source);
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\t", " ").replace("\n", " ").replace("\r", " ");
    }
}
