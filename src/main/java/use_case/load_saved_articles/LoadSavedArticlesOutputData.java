package use_case.load_saved_articles;

import java.util.List;

import entity.Article;

public class LoadSavedArticlesOutputData {
    private final String username;
    private final List<Article> savedArticles;

    public LoadSavedArticlesOutputData(String username,
                                       List<Article> savedArticles) {
        this.username = username;
        this.savedArticles = savedArticles;
    }

    public String getUsername() {
        return username;
    }

    public List<Article> getSavedArticles() {
        return savedArticles;
    }
}
