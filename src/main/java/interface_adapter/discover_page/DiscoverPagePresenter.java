package interface_adapter.discover_page;

import use_case.discover_page.DiscoverPageOutputBoundary;
import use_case.discover_page.DiscoverPageOutputData;

import java.util.Collections;

public class DiscoverPagePresenter implements DiscoverPageOutputBoundary {

    private final DiscoverPageViewModel discoverPageViewModel;

    public DiscoverPagePresenter(DiscoverPageViewModel discoverPageViewModel) {
        this.discoverPageViewModel = discoverPageViewModel;
    }

    @Override
    public void prepareSuccessView(DiscoverPageOutputData outputData) {
        DiscoverPageState state = discoverPageViewModel.getState();
        state.setArticles(outputData.getArticles());
        state.setCurrentTopics(outputData.getTopics());
        state.setCurrentPage(outputData.getPage());
        state.setMessage(null);
        state.setHasNoHistory(false);
        state.setHasNoArticles(false);
        discoverPageViewModel.firePropertyChange();
    }

    @Override
    public void prepareNoHistoryView(String message) {
        DiscoverPageState state = discoverPageViewModel.getState();
        state.setMessage(message);
        state.setHasNoHistory(true);
        state.setHasNoArticles(false);
        state.setArticles(Collections.emptyList());
        discoverPageViewModel.firePropertyChange();
    }

    @Override
    public void prepareNoArticlesView(String message) {
        DiscoverPageState state = discoverPageViewModel.getState();
        state.setMessage(message);
        state.setHasNoArticles(true);
        state.setHasNoHistory(false);
        state.setArticles(Collections.emptyList());
        discoverPageViewModel.firePropertyChange();
    }
}

