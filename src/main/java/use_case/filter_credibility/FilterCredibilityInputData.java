package use_case.filter_credibility;

import entity.Article;
import java.util.List;

/**
 * Input data for filtering articles by trust score level.
 * Contains the list of articles to filter and the desired trust level.
 */
public class FilterCredibilityInputData {
    private final List<Article> articles;
    private final String filterLevel; // "High", "Medium", "Low", or "All"/null for all articles

    public FilterCredibilityInputData(List<Article> articles, String filterLevel) {
        this.articles = articles;
        this.filterLevel = filterLevel;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public String getFilterLevel() {
        return filterLevel;
    }
}
