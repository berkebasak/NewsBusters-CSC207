package use_case.filter_credibility;

import entity.Article;
import java.util.List;
import java.util.Set;

/**
 * Input data for filtering articles by trust score level.
 * Contains the list of articles to filter and the desired trust levels.
 */
public class FilterCredibilityInputData {
    private final List<Article> articles;
    private final Set<String> filterLevels; // Set of "High", "Medium", "Low" - empty set means show all

    public FilterCredibilityInputData(List<Article> articles, Set<String> filterLevels) {
        this.articles = articles;
        this.filterLevels = filterLevels;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Set<String> getFilterLevels() {
        return filterLevels;
    }
}
