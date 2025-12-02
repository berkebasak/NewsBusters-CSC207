package interface_adapter.load_saved_articles;

import java.util.ArrayList;
import java.util.List;

import entity.Article;

public class LoadSavedArticlesState {
    private String username = "";
    private List<Article> savedArticles = new ArrayList<>();
    private String error;

    public LoadSavedArticlesState() {
    }

    public LoadSavedArticlesState(LoadSavedArticlesState copy) {
        this.username = copy.username;
        this.savedArticles = new ArrayList<>(copy.savedArticles);
        this.error = copy.error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Article> getSavedArticles() {
        return savedArticles;
    }

    public void setSavedArticles(List<Article> savedArticles) {
        this.savedArticles = savedArticles;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

