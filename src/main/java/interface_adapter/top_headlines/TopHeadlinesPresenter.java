package interface_adapter.top_headlines;

import use_case.top_headlines.TopHeadlinesOutputBoundary;
import use_case.top_headlines.TopHeadlinesOutputData;

public class TopHeadlinesPresenter implements TopHeadlinesOutputBoundary {
    private final TopHeadlinesViewModel viewModel;

    public TopHeadlinesPresenter(TopHeadlinesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(TopHeadlinesOutputData outputData) {
        var state = viewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setError(null);
        state.setArticleSourceLabel("New Articles");
        viewModel.firePropertyChange();
    }

    @Override
    public void presentAlternative(TopHeadlinesOutputData outputData, String message) {
        var state = viewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setError(message);
        state.setArticleSourceLabel("Saved Articles");
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.getState().setError(error);
        viewModel.firePropertyChange();
    }
}
