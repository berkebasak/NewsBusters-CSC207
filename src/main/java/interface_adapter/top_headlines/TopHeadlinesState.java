package interface_adapter.top_headlines;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesState {
    private List<Article> articles = new ArrayList<>();
    private String error;
    private String articleSourceLabel = "New Articles";
    private List<Article> originalArticles = new ArrayList<>(); // Stores unfiltered articles
    private java.util.Set<String> currentFilterLevels = new java.util.HashSet<>(); // Set of "High", "Medium", "Low" - empty means no filter

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

    public String getArticleSourceLabel() {
        return articleSourceLabel;
    }

    public void setArticleSourceLabel(String articleSourceLabel) {
        this.articleSourceLabel = articleSourceLabel;
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
