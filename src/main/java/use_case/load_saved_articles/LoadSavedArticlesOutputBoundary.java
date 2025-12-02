package use_case.load_saved_articles;

/**
 * The {@code LoadSavedArticlesOutputBoundary} defines the output methods that the
 * Load Saved Articles use case must call to update the user interface.
 *
 * <p>
 * Implementations of this interface are responsible for presenting the results of the
 * use case to the ViewModel or directly to the UI layer, depending on the architecture.
 */
public interface LoadSavedArticlesOutputBoundary {

    /**
     * Prepares the view for a successful load operation, passing the relevant output data.
     *
     * @param outputData the data containing the loaded articles and associated user information
     */
    void prepareSuccessView(LoadSavedArticlesOutputData outputData);

    /**
     * Prepares the view when loading saved articles fails by supplying an error message.
     *
     * @param errorMessage the message describing the reason for failure
     */
    void prepareFailView(String errorMessage);
}

