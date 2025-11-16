package use_case.discover_page;

import entity.Article;
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
            List<Article> readingHistory = dataAccessObject.getReadingHistory();

            if (readingHistory == null || readingHistory.isEmpty()) {
                presenter.prepareNoHistoryView("Save articles to personalize your Discover feed.");
                return;
            }

            Set<String> topics = dataAccessObject.extractTopTopics(readingHistory, 5);

            if (topics == null || topics.isEmpty()) {
                presenter.prepareNoHistoryView("Save articles to personalize your Discover feed.");
                return;
            }

            List<Article> discoveredArticles = dataAccessObject.searchByTopics(topics);

            if (discoveredArticles == null || discoveredArticles.isEmpty()) {
                presenter.prepareNoArticlesView("No new articles available.");
                return;
            }

            DiscoverPageOutputData outputData = new DiscoverPageOutputData(discoveredArticles, topics);
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareNoArticlesView("No new articles available.");
        }
    }
}
