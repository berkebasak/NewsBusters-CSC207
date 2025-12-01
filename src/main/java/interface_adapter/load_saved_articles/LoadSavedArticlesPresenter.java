package interface_adapter.load_saved_articles;

import interface_adapter.ViewManagerModel;
import use_case.load_saved_articles.LoadSavedArticlesOutputBoundary;
import use_case.load_saved_articles.LoadSavedArticlesOutputData;

public class LoadSavedArticlesPresenter implements LoadSavedArticlesOutputBoundary {

    private final LoadSavedArticlesViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public LoadSavedArticlesPresenter(LoadSavedArticlesViewModel viewModel,
                                      ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoadSavedArticlesOutputData outputData) {
        final LoadSavedArticlesState state = viewModel.getState();
        state.setUsername(outputData.getUsername());
        state.setSavedArticles(outputData.getSavedArticles());
        state.setError(null);

        viewModel.setState(state);
        viewModel.firePropertyChange();

        viewManagerModel.changeView(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoadSavedArticlesState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

}
