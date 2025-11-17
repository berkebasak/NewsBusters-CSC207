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
        viewModel.getState().setArticles(outputData.getArticles());
        viewModel.firePropertyChange();
    }
}
