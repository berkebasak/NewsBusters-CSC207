package use_case.top_headlines;


import entity.Article;

import java.util.List;
import java.util.ArrayList;

public class TopHeadlinesInteractor implements TopHeadlinesInputBoundary {

    private final TopHeadlinesUserDataAccessInterface dataAccess;
    private final TopHeadlinesOutputBoundary presenter;

    public TopHeadlinesInteractor(TopHeadlinesUserDataAccessInterface dataAccess,
                                  TopHeadlinesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(TopHeadlinesInputData inputData) {
        List<Article> articles = dataAccess.fetchTopHeadlines(inputData.getUserPreferences());

        if (articles != null && articles.size() > 20) {
            articles = new ArrayList<>(articles.subList(0, 20));
        }

        presenter.present(new TopHeadlinesOutputData(articles));
    }
}
