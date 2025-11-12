package use_case.search_news;

/**
 * The Input Boundary for the Search News use case.
 * Called by the Controller to start the search.
 */
public interface SearchNewsInputBoundary {

    /**
     * Starts the search with user input.
     * @param inputData keyword entered by the user
     */
    void execute(SearchNewsInputData inputData);
}
