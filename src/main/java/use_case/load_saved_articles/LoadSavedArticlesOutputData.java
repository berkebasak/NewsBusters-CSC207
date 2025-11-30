package use_case.load_saved_articles;

import entity.Article;

import java.util.List;

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
