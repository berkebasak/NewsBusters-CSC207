package use_case.load_saved_articles;

/**
 * The {@code LoadSavedArticlesInputBoundary} defines the input method for the
 * Load Saved Articles use case.
 *
 * <p>
 * This boundary is called by the controller and signals the interactor to execute
 * the business logic required to retrieve a user's saved articles.
 */
public interface LoadSavedArticlesInputBoundary {

    /**
     * Executes the load saved articles use case using the provided input data.
     *
     * @param inputData the input data containing the necessary information
     *                  (e.g., the username) to perform the operation
     */
    void execute(LoadSavedArticlesInputData inputData);
}

