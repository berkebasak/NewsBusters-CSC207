package interface_adapter.discover_page;

import entity.UserPreferences;
import interface_adapter.login.LoginViewModel;
import use_case.discover_page.DiscoverPageInputBoundary;
import use_case.discover_page.DiscoverPageInputData;

import java.util.Set;

public class DiscoverPageController {
    private final DiscoverPageInputBoundary discoverPageUseCaseInteractor;
    private final DiscoverPageViewModel discoverPageViewModel;
    private final LoginViewModel loginViewModel;

    public DiscoverPageController(DiscoverPageInputBoundary discoverPageUseCaseInteractor,
                                  DiscoverPageViewModel discoverPageViewModel,
                                  LoginViewModel loginViewModel) {
        this.discoverPageUseCaseInteractor = discoverPageUseCaseInteractor;
        this.discoverPageViewModel = discoverPageViewModel;
    }

    public void execute(UserPreferences userPreferences) {
        DiscoverPageState state = discoverPageViewModel.getState();
        Set<String> currentTopics = state.getCurrentTopics();
        int currentPage = state.getCurrentPage();
        String username = loginViewModel.getState().getUsername();
        DiscoverPageInputData inputData = new DiscoverPageInputData(currentTopics, currentPage, username);
        DiscoverPageInputData inputData = new DiscoverPageInputData(currentTopics, currentPage, userPreferences);
        discoverPageUseCaseInteractor.execute(inputData);
    }
}

