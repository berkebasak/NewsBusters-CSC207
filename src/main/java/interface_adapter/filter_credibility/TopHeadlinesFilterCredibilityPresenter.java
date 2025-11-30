package interface_adapter.filter_credibility;

import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.filter_credibility.FilterCredibilityOutputBoundary;
import use_case.filter_credibility.FilterCredibilityOutputData;

/**
 * Presenter for filtering credibility scores in the Top Headlines view.
 * Updates the TopHeadlinesViewModel with filtered articles or error messages.
 */
public class TopHeadlinesFilterCredibilityPresenter implements FilterCredibilityOutputBoundary {
    private final TopHeadlinesViewModel viewModel;

    public TopHeadlinesFilterCredibilityPresenter(TopHeadlinesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(FilterCredibilityOutputData outputData) {
        var state = viewModel.getState();
        state.setArticles(outputData.getFilteredArticles());
        state.setError(null); // Clear any previous errors
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(String errorMessage) {
        var state = viewModel.getState();
        state.setError(errorMessage);
        viewModel.firePropertyChange();
    }
}
