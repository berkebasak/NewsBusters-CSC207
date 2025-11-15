package use_case.search_news;

/**
 * Output Boundary interface for the Search News use case.
 * Used by the Interactor to send results to the Presenter.
 */
public interface SearchNewsOutputBoundary {

    /**
     * Shows the successful search results.
     * @param outputData the data containing the search results
     */
    void prepareSuccessView(SearchNewsOutputData outputData);

    /**
     * Shows an error message if the search fails.
     * @param message the error message to display
     */
    void prepareFailView(String message);

}
