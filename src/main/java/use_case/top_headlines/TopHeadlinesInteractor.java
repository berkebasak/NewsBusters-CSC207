package use_case.top_headlines;

import entity.Article;
import data_access.DBUserDataAccessObject;
import java.util.List;
import java.util.ArrayList;

public class TopHeadlinesInteractor implements TopHeadlinesInputBoundary {

    private final TopHeadlinesUserDataAccessInterface dataAccess;
    private final DBUserDataAccessObject dbUserDataAccessObject;
    private final TopHeadlinesOutputBoundary presenter;

    public TopHeadlinesInteractor(TopHeadlinesUserDataAccessInterface dataAccess,
                                  DBUserDataAccessObject dbUserDataAccessObject,
                                  TopHeadlinesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.dbUserDataAccessObject = dbUserDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(TopHeadlinesInputData inputData) {

        // Main Flow: Fatch articles from the API
        List<Article> articles = dataAccess.fetchTopHeadlines();

        boolean apiReturnedValidData = articles != null && !articles.isEmpty();

        if (apiReturnedValidData) {
            if (articles.size() > 20) {
                articles = new ArrayList<>(articles.subList(0, 20));
            }

            TopHeadlinesOutputData outputData =
                    new TopHeadlinesOutputData(articles);

            presenter.present(outputData);
            return;
        }

        // Alternative Flow, No Internet Connection, API Failure
        List<Article> savedArticles = dbUserDataAccessObject.getReadingHistory();
        List<Article> trimmedSaved = savedArticles;

        if (trimmedSaved != null && trimmedSaved.size() > 20) {
            trimmedSaved = new ArrayList<>(trimmedSaved.subList(0, 20));
        }

        TopHeadlinesOutputData fallbackData =
                new TopHeadlinesOutputData(trimmedSaved);

        presenter.presentAlternative(
                fallbackData,
                "No internet connection or API failure.\nShowing saved articles instead."
        );
    }
}
