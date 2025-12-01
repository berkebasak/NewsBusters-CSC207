package interface_adapter.sort_headlines;

import interface_adapter.top_headlines.TopHeadlinesState;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.sort_headlines.SortHeadlinesOutputBoundary;
import use_case.sort_headlines.SortHeadlinesOutputData;

public class SortHeadlinesPresenter implements SortHeadlinesOutputBoundary {
    private final TopHeadlinesViewModel viewModel;

    public SortHeadlinesPresenter(TopHeadlinesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SortHeadlinesOutputData outputData) {
        TopHeadlinesState state = viewModel.getState();
        state.setArticles(outputData.getArticles());
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        TopHeadlinesState state = viewModel.getState();
        state.setError(error);
        viewModel.firePropertyChange();
    }
}
