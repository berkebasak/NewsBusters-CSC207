package interface_adapter.top_headlines;

import use_case.top_headlines.TopHeadlinesInputBoundary;
import use_case.top_headlines.TopHeadlinesInputData;

public class TopHeadlinesController {
    private final TopHeadlinesInputBoundary interactor;

    public TopHeadlinesController(TopHeadlinesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void fetchHeadlines() {
        TopHeadlinesInputData inputData = new TopHeadlinesInputData("top");
        interactor.execute(inputData);
    }
}
