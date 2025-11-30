package interface_adapter.discover_page;

import entity.UserPreferences;
import use_case.discover_page.DiscoverPageInputBoundary;
import use_case.discover_page.DiscoverPageInputData;

import java.util.Set;

public class DiscoverPageController {
    private final DiscoverPageInputBoundary discoverPageUseCaseInteractor;
    private final DiscoverPageViewModel discoverPageViewModel;

    public DiscoverPageController(DiscoverPageInputBoundary discoverPageUseCaseInteractor,
                                  DiscoverPageViewModel discoverPageViewModel) {
        this.discoverPageUseCaseInteractor = discoverPageUseCaseInteractor;
        this.discoverPageViewModel = discoverPageViewModel;
    }

    public void execute(UserPreferences userPreferences) {
        DiscoverPageState state = discoverPageViewModel.getState();
        Set<String> currentTopics = state.getCurrentTopics();
        int currentPage = state.getCurrentPage();
        DiscoverPageInputData inputData = new DiscoverPageInputData(currentTopics, currentPage, userPreferences);
        discoverPageUseCaseInteractor.execute(inputData);
    }
}

