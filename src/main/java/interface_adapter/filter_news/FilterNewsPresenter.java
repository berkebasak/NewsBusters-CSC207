package interface_adapter.filter_news;

import interface_adapter.top_headlines.TopHeadlinesState;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.filter_news.FilterNewsOutputBoundary;
import use_case.filter_news.FilterNewsOutputData;

import java.util.Collections;

/**
 * Presenter for the Filter News use case.
 * Updates the SAME TopHeadlinesViewModel so the filtered results
 * appear in TopHeadlinesView.
 */
public class FilterNewsPresenter implements FilterNewsOutputBoundary {

    private final TopHeadlinesViewModel topHeadlinesViewModel;

    /**
     * Creates a FilterNewsPresenter.
     * @param topHeadlinesViewModel the shared ViewModel used by TopHeadlinesView
     */
    public FilterNewsPresenter(TopHeadlinesViewModel topHeadlinesViewModel) {
        this.topHeadlinesViewModel = topHeadlinesViewModel;
    }

    /**
     * Called when filtering succeeds.
     * @param outputData the filtered articles
     */
    @Override
    public void prepareSuccessView(FilterNewsOutputData outputData) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setError(null);
        topHeadlinesViewModel.firePropertyChange();
    }

    /**
     * Called when filtering fails.
     * @param errorMessage a short message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();
        state.setArticles(Collections.emptyList());
        state.setError(errorMessage);
        topHeadlinesViewModel.firePropertyChange();
    }
}
