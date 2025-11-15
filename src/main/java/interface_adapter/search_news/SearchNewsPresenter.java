package interface_adapter.search_news;

import interface_adapter.top_headlines.TopHeadlinesState;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.search_news.SearchNewsOutputBoundary;
import use_case.search_news.SearchNewsOutputData;


import java.util.Collections;

/**
 * The Presenter for the Search News Use Case.
 * It updates the SAME ViewModel used by TopHeadlinesView,
 * so the results show up in the headlines list.
 */
public class SearchNewsPresenter implements SearchNewsOutputBoundary {

    private final TopHeadlinesViewModel topHeadlinesViewModel;

    /**
     * Builds a SearchNewsPresenter.
     * @param topHeadlinesViewModel the shared ViewModel used by TopHeadlinesView
     */
    public SearchNewsPresenter(TopHeadlinesViewModel topHeadlinesViewModel) {
        this.topHeadlinesViewModel = topHeadlinesViewModel;
    }

    /**
     * Updates the state on success and notifies the view.
     */
    @Override
    public void prepareSuccessView(SearchNewsOutputData outputData) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setError(null);
        topHeadlinesViewModel.firePropertyChange();
    }

    /**
     * Updates the state on failure and notifies the view.
     */
    @Override
    public void prepareFailView(String error) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();
        state.setArticles(Collections.emptyList());
        state.setError(error);
        topHeadlinesViewModel.firePropertyChange();
    }
}
