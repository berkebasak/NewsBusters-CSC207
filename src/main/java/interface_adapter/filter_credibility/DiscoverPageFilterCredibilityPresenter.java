package interface_adapter.filter_credibility;

import interface_adapter.discover_page.DiscoverPageViewModel;
import use_case.filter_credibility.FilterCredibilityOutputBoundary;
import use_case.filter_credibility.FilterCredibilityOutputData;

/**
 * Presenter for filtering credibility scores in the Discover Page view.
 * Updates the DiscoverPageViewModel with filtered articles or error messages.
 */
public class DiscoverPageFilterCredibilityPresenter implements FilterCredibilityOutputBoundary {
    private final DiscoverPageViewModel viewModel;

    public DiscoverPageFilterCredibilityPresenter(DiscoverPageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(FilterCredibilityOutputData outputData) {
        var state = viewModel.getState();
        state.setArticles(outputData.getFilteredArticles());
        state.setMessage(null); // Clear any previous messages
        state.setHasNoArticles(false);
        state.setHasNoHistory(false);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(String errorMessage) {
        var state = viewModel.getState();
        state.setMessage(errorMessage);
        state.setHasNoArticles(true); // Show message view
        state.setHasNoHistory(false);
        viewModel.firePropertyChange();
    }
}
