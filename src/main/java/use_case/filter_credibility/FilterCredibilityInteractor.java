package use_case.filter_credibility;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for filtering articles by trust score level.
 * Implements the business logic for filtering articles based on their credibility scores.
 */
public class FilterCredibilityInteractor implements FilterCredibilityInputBoundary {
    private final FilterCredibilityOutputBoundary presenter;

    public FilterCredibilityInteractor(FilterCredibilityOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(FilterCredibilityInputData inputData) {
        List<Article> articles = inputData.getArticles();
        String filterLevel = inputData.getFilterLevel();

        // Alternative Flow 1: Check if all articles have trust scores
        if (!allArticlesHaveScores(articles)) {
            presenter.presentError("Generate all scores to filter.");
            return;
        }

        // If filterLevel is "All" or null, return all articles
        if (filterLevel == null || "All".equalsIgnoreCase(filterLevel)) {
            FilterCredibilityOutputData outputData = new FilterCredibilityOutputData(articles, "All");
            presenter.presentSuccess(outputData);
            return;
        }

        // Filter articles by the selected trust level
        List<Article> filteredArticles = filterByLevel(articles, filterLevel);

        // Alternative Flow 2: Check if any articles match the filter
        if (filteredArticles.isEmpty()) {
            presenter.presentError("No articles found at this trust level.");
            return;
        }

        // Main Flow: Successfully filtered articles
        FilterCredibilityOutputData outputData = new FilterCredibilityOutputData(filteredArticles, filterLevel);
        presenter.presentSuccess(outputData);
    }

    /**
     * Checks if all articles have generated credibility scores.
     * An article has a score if it has a non-null credibilityScore and
     * a confidenceLevel that is not "Unknown".
     * 
     * @param articles the list of articles to check
     * @return true if all articles have scores, false otherwise
     */
    private boolean allArticlesHaveScores(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return false;
        }

        for (Article article : articles) {
            if (article.getCredibilityScore() == null) {
                return false;
            }
            String level = article.getConfidenceLevel();
            if (level == null || "Unknown".equalsIgnoreCase(level)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Filters articles by the specified trust level.
     * 
     * @param articles the list of articles to filter
     * @param filterLevel the trust level to filter by ("High", "Medium", or "Low")
     * @return a list of articles matching the filter level
     */
    private List<Article> filterByLevel(List<Article> articles, String filterLevel) {
        List<Article> filtered = new ArrayList<>();
        
        for (Article article : articles) {
            String articleLevel = article.getConfidenceLevel();
            if (articleLevel != null && articleLevel.equalsIgnoreCase(filterLevel)) {
                filtered.add(article);
            }
        }
        
        return filtered;
    }
}
