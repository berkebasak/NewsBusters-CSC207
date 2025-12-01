package use_case.filter_credibility;

import entity.Article;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<String> filterLevels = inputData.getFilterLevels();

        // Alternative Flow 1: Check if all articles have trust scores
        if (!allArticlesHaveScores(articles)) {
            presenter.presentError("Generate all scores to filter.");
            return;
        }

        // If filterLevels is empty or null, return all articles
        if (filterLevels == null || filterLevels.isEmpty()) {
            FilterCredibilityOutputData outputData = new FilterCredibilityOutputData(articles, new HashSet<>());
            presenter.presentSuccess(outputData);
            return;
        }

        // Filter articles by the selected trust levels (matching any of the selected levels)
        List<Article> filteredArticles = filterByLevels(articles, filterLevels);

        // Alternative Flow 2: Check if any articles match the filter
        if (filteredArticles.isEmpty()) {
            presenter.presentError("No articles found at the selected trust levels.");
            return;
        }

        // Main Flow: Successfully filtered articles
        FilterCredibilityOutputData outputData = new FilterCredibilityOutputData(filteredArticles, filterLevels);
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
     * Filters articles by the specified trust levels.
     * Returns articles that match any of the selected levels.
     * 
     * @param articles the list of articles to filter
     * @param filterLevels the set of trust levels to filter by ("High", "Medium", "Low")
     * @return a list of articles matching any of the filter levels
     */
    private List<Article> filterByLevels(List<Article> articles, Set<String> filterLevels) {
        List<Article> filtered = new ArrayList<>();
        
        // Normalize filter levels to case-insensitive comparison
        Set<String> normalizedLevels = new HashSet<>();
        for (String level : filterLevels) {
            if (level != null) {
                normalizedLevels.add(level.toLowerCase());
            }
        }
        
        for (Article article : articles) {
            String articleLevel = article.getConfidenceLevel();
            if (articleLevel != null && normalizedLevels.contains(articleLevel.toLowerCase())) {
                filtered.add(article);
            }
        }
        
        return filtered;
    }
}
