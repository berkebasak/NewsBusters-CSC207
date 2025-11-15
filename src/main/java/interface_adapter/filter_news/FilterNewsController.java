package interface_adapter.filter_news;

import use_case.filter_news.FilterNewsInputBoundary;
import use_case.filter_news.FilterNewsInputData;

import java.util.List;

/**
 * Controller for the Filter News use case.
 * Sends the user's selected topics to the Interactor.
 */
public class FilterNewsController {

    private final FilterNewsInputBoundary filterNewsUseCaseInteractor;

    /**
     * Creates a new FilterNewsController.
     * @param filterNewsUseCaseInteractor the Interactor that performs the filtering
     */
    public FilterNewsController(FilterNewsInputBoundary filterNewsUseCaseInteractor) {
        this.filterNewsUseCaseInteractor = filterNewsUseCaseInteractor;
    }

    /**
     * Tells the Interactor to filter by the given topics.
     * @param topics the topics chosen by the user
     */
    public void execute(List<String> topics) {
        final FilterNewsInputData filterNewsInputData = new FilterNewsInputData(topics);
        filterNewsUseCaseInteractor.execute(filterNewsInputData);
    }
}
