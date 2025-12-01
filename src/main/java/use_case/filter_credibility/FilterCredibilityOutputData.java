package use_case.filter_credibility;

import entity.Article;
import java.util.List;
import java.util.Set;

/**
 * Output data for the filter credibility use case.
 * Contains the filtered articles and the filter levels that were applied.
 */
public class FilterCredibilityOutputData {
    private final List<Article> filteredArticles;
    private final Set<String> filterLevels;

    public FilterCredibilityOutputData(List<Article> filteredArticles, Set<String> filterLevels) {
        this.filteredArticles = filteredArticles;
        this.filterLevels = filterLevels;
    }

    public List<Article> getFilteredArticles() {
        return filteredArticles;
    }

    public Set<String> getFilterLevels() {
        return filterLevels;
    }
}
