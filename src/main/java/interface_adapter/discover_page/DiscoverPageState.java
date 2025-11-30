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
    private List<Article> originalArticles = new ArrayList<>(); // Stores unfiltered articles
    private String currentFilterLevel; // "High", "Medium", "Low", "All", or null

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

    /**
     * Gets the original unfiltered articles list.
     * @return the original articles list
     */
    public List<Article> getOriginalArticles() {
        return originalArticles;
    }

    /**
     * Sets the original unfiltered articles list.
     * @param originalArticles the original articles list to store
     */
    public void setOriginalArticles(List<Article> originalArticles) {
        this.originalArticles = originalArticles != null ? new ArrayList<>(originalArticles) : new ArrayList<>();
    }

    /**
     * Gets the current filter level.
     * @return the current filter level ("High", "Medium", "Low", "All", or null)
     */
    public String getCurrentFilterLevel() {
        return currentFilterLevel;
    }

    /**
     * Sets the current filter level.
     * @param currentFilterLevel the filter level to set ("High", "Medium", "Low", "All", or null)
     */
    public void setCurrentFilterLevel(String currentFilterLevel) {
        this.currentFilterLevel = currentFilterLevel;
    }

    /**
     * Checks if articles are currently filtered.
     * @return true if filtering is active (filter level is not null and not "All"), false otherwise
     */
    public boolean isFiltered() {
        return currentFilterLevel != null && !"All".equalsIgnoreCase(currentFilterLevel);
    }
}

