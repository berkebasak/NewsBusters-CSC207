package use_case.filter_news;

/**
 * Output Boundary for the Filter News use case.
 */
public interface FilterNewsOutputBoundary {

    /**
     * Prepares the success view when articles match the topics.
     * @param outputData the data containing the results
     */
    void prepareSuccessView(FilterNewsOutputData outputData);

    /**
     * Prepares the failure view when no articles are found.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);
}
