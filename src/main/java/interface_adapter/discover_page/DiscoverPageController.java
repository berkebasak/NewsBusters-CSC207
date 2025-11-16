package interface_adapter.discover_page;

import use_case.discover_page.DiscoverPageInputBoundary;
import use_case.discover_page.DiscoverPageInputData;

public class DiscoverPageController {
    private final DiscoverPageInputBoundary discoverPageUseCaseInteractor;

    public DiscoverPageController(DiscoverPageInputBoundary discoverPageUseCaseInteractor) {
        this.discoverPageUseCaseInteractor = discoverPageUseCaseInteractor;
    }

    public void execute() {
        DiscoverPageInputData inputData = new DiscoverPageInputData();
        discoverPageUseCaseInteractor.execute(inputData);
    }
}

