package use_case.filter_credibility;

/**
 * Interface for the output boundary of the filter credibility use case.
 * Implemented by the presenter to handle success and error cases.
 */
public interface FilterCredibilityOutputBoundary {
    /**
     * Presents the successfully filtered articles.
     * 
     * @param outputData the output data containing filtered articles
     */
    void presentSuccess(FilterCredibilityOutputData outputData);

    /**
     * Presents an error message when filtering fails.
     * 
     * @param errorMessage the error message to display
     */
    void presentError(String errorMessage);
}
