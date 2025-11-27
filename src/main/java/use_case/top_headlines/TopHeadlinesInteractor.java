package use_case.top_headlines;


import entity.Article;

import java.util.List;

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
        List<Article> articles = dataAccess.fetchTopHeadlines();
        presenter.present(new TopHeadlinesOutputData(articles));
    }
}
