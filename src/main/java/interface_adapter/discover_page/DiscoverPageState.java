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
    private List<Article> originalArticles = new ArrayList<>();
    private java.util.Set<String> currentFilterLevels = new java.util.HashSet<>();

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
     * Gets the current filter levels.
     * @return the set of current filter levels ("High", "Medium", "Low") - empty set means no filter
     */
    public java.util.Set<String> getCurrentFilterLevels() {
        return currentFilterLevels;
    }

    /**
     * Sets the current filter levels.
     * @param currentFilterLevels the set of filter levels to set ("High", "Medium", "Low") - empty set means no filter
     */
    public void setCurrentFilterLevels(java.util.Set<String> currentFilterLevels) {
        this.currentFilterLevels = currentFilterLevels != null ? new java.util.HashSet<>(currentFilterLevels) : new java.util.HashSet<>();
    }

    /**
     * Checks if articles are currently filtered.
     * @return true if filtering is active (filter levels set is not empty), false otherwise
     */
    public boolean isFiltered() {
        return currentFilterLevels != null && !currentFilterLevels.isEmpty();
    }
}

