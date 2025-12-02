package interface_adapter.search_news;

import use_case.search_news.SearchNewsInputBoundary;
import use_case.search_news.SearchNewsInputData;

/**
 * Controller for the Search News use case.
 */
public class SearchNewsController {

    private final SearchNewsInputBoundary searchNewsInteractor;

    public SearchNewsController(SearchNewsInputBoundary searchNewsInteractor) {
        this.searchNewsInteractor = searchNewsInteractor;
    }

    /**
     * Executes the Search News use case with the given keyword.
     *
     * @param keyword the keyword entered by the user
     */
    public void execute(String keyword) {
        SearchNewsInputData inputData = new SearchNewsInputData(keyword);
        searchNewsInteractor.execute(inputData);
    }
}
