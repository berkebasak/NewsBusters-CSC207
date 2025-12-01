package use_case.top_headlines;

import entity.Article;

import java.util.List;
import java.util.ArrayList;

public class TopHeadlinesInteractor implements TopHeadlinesInputBoundary {

    private final TopHeadlinesUserDataAccessInterface dataAccess;
    private final TopHeadlinesOutputBoundary presenter;

    private List<Article> currentArticles = new ArrayList<>();

    public TopHeadlinesInteractor(TopHeadlinesUserDataAccessInterface dataAccess,
            TopHeadlinesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(TopHeadlinesInputData inputData) {
        try {
            List<Article> articles = dataAccess.fetchTopHeadlines();
            if (articles != null && articles.size() > 20) {
                currentArticles = new ArrayList<>(articles.subList(0, 20));
            } else if (articles != null) {
                currentArticles = new ArrayList<>(articles);
            } else {
                currentArticles = new ArrayList<>();
            }

            if (currentArticles.isEmpty()) {
                presenter.prepareFailView("No top headlines found.");
            } else {
                presenter.present(new TopHeadlinesOutputData(currentArticles));
            }
        } catch (Exception e) {
            presenter.prepareFailView("Failed to fetch top headlines: " + e.getMessage());
        }
    }
}
