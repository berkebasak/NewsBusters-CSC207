package interface_adapter.filter_credibility;

import entity.Article;
import use_case.filter_credibility.FilterCredibilityInputBoundary;
import use_case.filter_credibility.FilterCredibilityInputData;

import java.util.List;
import java.util.Set;

/**
 * Controller for the filter credibility use case.
 * Handles requests from views to filter articles by trust score level.
 */
public class FilterCredibilityController {
    private final FilterCredibilityInputBoundary interactor;

    public FilterCredibilityController(FilterCredibilityInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Filters articles by the specified trust score levels.
     * 
     * @param articles the list of articles to filter
     * @param filterLevels the set of trust levels to filter by ("High", "Medium", "Low")
     *                      Empty set means show all articles
     */
    public void filterArticles(List<Article> articles, Set<String> filterLevels) {
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }
}
