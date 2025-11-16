package interface_adapter.discover_page;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class DiscoverPageState {
    private List<Article> articles = new ArrayList<>();
    private String message;
    private boolean hasNoHistory;
    private boolean hasNoArticles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getHasNoHistory() {
        return hasNoHistory;
    }

    public void setHasNoHistory(boolean hasNoHistory) {
        this.hasNoHistory = hasNoHistory;
    }

    public boolean getHasNoArticles() {
        return hasNoArticles;
    }

    public void setHasNoArticles(boolean hasNoArticles) {
        this.hasNoArticles = hasNoArticles;
    }
}

