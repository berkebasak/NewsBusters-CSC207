package use_case.discover_page;

import entity.Article;
import entity.UserPreferences;

import java.util.List;
import java.util.Set;

public class DiscoverPageInteractor implements DiscoverPageInputBoundary {

    private final DiscoverPageDataAccessInterface dataAccessObject;
    private final DiscoverPageOutputBoundary presenter;

    public DiscoverPageInteractor(DiscoverPageDataAccessInterface dataAccessObject,
                                  DiscoverPageOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(DiscoverPageInputData inputData) {
        try {
            String username = inputData.getUsername();
            List<Article> readingHistory = dataAccessObject.getReadingHistory(username);

            if (readingHistory == null || readingHistory.isEmpty()) {
                presenter.prepareNoHistoryView("Save articles to personalize your Discover feed.");
                return;
            }

            Set<String> newTopics = dataAccessObject.extractTopTopics(readingHistory, 5);

            if (newTopics == null || newTopics.isEmpty()) {
                presenter.prepareNoHistoryView("Save articles to personalize your Discover feed.");
                return;
            }

            // Check if topics have changed
            Set<String> currentTopics = inputData.getCurrentTopics();
            int pageToUse;
            
            if (currentTopics == null || currentTopics.isEmpty() || !currentTopics.equals(newTopics)) {
                // Topics changed, reset to page 0
                pageToUse = 0;
            } else {
                // Topics are the same, increment page for next set of articles
                pageToUse = inputData.getCurrentPage() + 1;
            }

            UserPreferences userPreferences = inputData.getUserPreferences();

            List<Article> discoveredArticles = dataAccessObject.searchByTopics(newTopics, pageToUse, userPreferences);

            if (discoveredArticles == null || discoveredArticles.isEmpty()) {
                // If no articles on this page, try page 0
                if (pageToUse > 0) {
                    discoveredArticles = dataAccessObject.searchByTopics(newTopics, 0, userPreferences);
                    pageToUse = 0;
                }
                
                if (discoveredArticles == null || discoveredArticles.isEmpty()) {
                    presenter.prepareNoArticlesView("No new articles available.");
                    return;
                }
            }

            DiscoverPageOutputData outputData = new DiscoverPageOutputData(discoveredArticles, newTopics, pageToUse);
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareNoArticlesView("No new articles available.");
        }
    }
}
