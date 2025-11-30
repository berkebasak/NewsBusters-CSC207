package use_case.filter_credibility;

import entity.Article;
import java.util.List;

/**
 * Output data for the filter credibility use case.
 * Contains the filtered articles and the filter level that was applied.
 */
public class FilterCredibilityOutputData {
    private final List<Article> filteredArticles;
    private final String filterLevel;

    public FilterCredibilityOutputData(List<Article> filteredArticles, String filterLevel) {
        this.filteredArticles = filteredArticles;
        this.filterLevel = filterLevel;
    }

    public List<Article> getFilteredArticles() {
        return filteredArticles;
    }

    public String getFilterLevel() {
        return filterLevel;
    }
}
