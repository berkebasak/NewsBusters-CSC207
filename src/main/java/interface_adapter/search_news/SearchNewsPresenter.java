package interface_adapter.search_news;

import use_case.search_news.SearchNewsOutputBoundary;
import use_case.search_news.SearchNewsOutputData;

/**
 * The Presenter for the Search News Use Case.
 */
public class SearchNewsPresenter implements SearchNewsOutputBoundary {

    private final SearchNewsViewModel searchNewsViewModel;

    /**
     * Builds a SearchNewsPresenter.
     * @param searchNewsViewModel the ViewModel for Search News
     */
    public SearchNewsPresenter(SearchNewsViewModel searchNewsViewModel) {
        this.searchNewsViewModel = searchNewsViewModel;
    }

    /**
     * Updates the state on success and notifies the view.
     * @param outputData the results of the search (keyword and articles)
     */
    @Override
    public void prepareSuccessView(SearchNewsOutputData outputData) {
        searchNewsViewModel.getState().setError(null);
        searchNewsViewModel.getState().setKeyword(outputData.getKeyword());
        searchNewsViewModel.getState().setArticles(outputData.getArticles());
        searchNewsViewModel.firePropertyChange(SearchNewsViewModel.STATE_PROPERTY);
    }

    /**
     * Updates the state on failure and notifies the view.
     * @param error the error message to display
     */
    @Override
    public void prepareFailView(String error) {
        searchNewsViewModel.getState().setArticles(null);
        searchNewsViewModel.getState().setError(error);
        searchNewsViewModel.firePropertyChange(SearchNewsViewModel.STATE_PROPERTY);
    }
}
