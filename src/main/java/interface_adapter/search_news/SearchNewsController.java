package interface_adapter.search_news;

import use_case.search_news.SearchNewsInputBoundary;
import use_case.search_news.SearchNewsInputData;

/**
 * The Controller for the Search News use case.
 * Receives the keyword from the UI and passed it to the Interactor
 */

public class SearchNewsController {
    private final SearchNewsInputBoundary searchNewsUseCaseInteractor;

    /**
     * Creates a new SearchNewsController.
     * @param searchNewsUseCaseInteractor the use case Interactor that runs the search logic
     */
    public SearchNewsController(SearchNewsInputBoundary searchNewsUseCaseInteractor) {
        this.searchNewsUseCaseInteractor = searchNewsUseCaseInteractor;
    }

    /**
     * Called when the user clicks the Search button or presses Enter.
     * @param keyword the keyword entered by the user
     */
    public void execute(String keyword) {
        final SearchNewsInputData searchNewsInputData = new SearchNewsInputData(keyword);
        searchNewsUseCaseInteractor.execute(searchNewsInputData);
    }

}
