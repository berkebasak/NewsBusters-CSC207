package interface_adapter.discover_page;

import entity.Article;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiscoverPageState {
    private List<Article> articles = new ArrayList<>();
    private String message;
    private boolean hasNoHistory;
    private boolean hasNoArticles;
    private Set<String> currentTopics = new java.util.HashSet<>();
    private int currentPage = 0;

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

    public Set<String> getCurrentTopics() {
        return currentTopics;
    }

    public void setCurrentTopics(Set<String> currentTopics) {
        this.currentTopics = currentTopics;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}

