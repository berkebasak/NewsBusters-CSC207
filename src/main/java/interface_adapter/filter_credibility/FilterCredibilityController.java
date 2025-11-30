package interface_adapter.filter_credibility;

import entity.Article;
import use_case.filter_credibility.FilterCredibilityInputBoundary;
import use_case.filter_credibility.FilterCredibilityInputData;

import java.util.List;

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
     * Filters articles by the specified trust score level.
     * 
     * @param articles the list of articles to filter
     * @param filterLevel the trust level to filter by ("High", "Medium", "Low", or "All"/null for all articles)
     */
    public void filterArticles(List<Article> articles, String filterLevel) {
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevel);
        interactor.execute(inputData);
    }
}
