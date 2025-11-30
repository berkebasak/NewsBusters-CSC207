package interface_adapter.top_headlines;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesState {
    private List<Article> articles = new ArrayList<>();
    private String error;
    private List<Article> originalArticles = new ArrayList<>(); // Stores unfiltered articles
    private String currentFilterLevel; // "High", "Medium", "Low", "All", or null

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     * @param error the message to set
     */
    public void setError(String error) {
        this.error = error;
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
