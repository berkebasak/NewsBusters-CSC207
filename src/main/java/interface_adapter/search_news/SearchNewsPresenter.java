package interface_adapter.search_news;

import interface_adapter.ViewManagerModel;
import use_case.search_news.SearchNewsOutputBoundary;
import use_case.search_news.SearchNewsOutputData;

import java.util.Collections;

/**
 * Presenter for the Search News use case.
 */
public class SearchNewsPresenter implements SearchNewsOutputBoundary {

    private final SearchNewsViewModel searchNewsViewModel;

    public SearchNewsPresenter(SearchNewsViewModel searchNewsViewModel) {
        this.searchNewsViewModel = searchNewsViewModel;
    }

    @Override
    public void prepareSuccessView(SearchNewsOutputData outputData) {
        SearchNewsState state = searchNewsViewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setError(null);
        state.setKeyword(outputData.getKeyword());
        state.setArticleSourceLabel("Search Results");
        searchNewsViewModel.setState(state);
        searchNewsViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String message) {
        SearchNewsState state = searchNewsViewModel.getState();
        state.setArticles(Collections.emptyList());
        state.setError(message);
        state.setArticleSourceLabel("Search Results");
        searchNewsViewModel.firePropertyChange();
    }
}
