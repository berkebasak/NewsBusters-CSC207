package interface_adapter.load_saved_articles;

import use_case.load_saved_articles.LoadSavedArticlesInputBoundary;
import use_case.load_saved_articles.LoadSavedArticlesInputData;

/**
 * The {@code LoadSavedArticlesController} receives input from the view layer
 * (typically a username string) and converts it into a format suitable for the
 * Load Saved Articles use case.
 *
 * <p>
 * This controller acts as the bridge between the UI and the interactor by creating
 * the required input data object and invoking the use case through its input boundary.
 */
public class LoadSavedArticlesController {

    /**
     * The interactor responsible for executing the Load Saved Articles use case.
     */
    private final LoadSavedArticlesInputBoundary interactor;

    /**
     * Constructs a {@code LoadSavedArticlesController} with the specified interactor.
     *
     * @param interactor the input boundary used to trigger the use case
     */
    public LoadSavedArticlesController(LoadSavedArticlesInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the Load Saved Articles use case using the provided username.
     *
     * @param username the username for which saved articles should be retrieved
     */
    public void execute(String username) {
        final LoadSavedArticlesInputData inputData = new LoadSavedArticlesInputData(username);
        interactor.execute(inputData);
    }
}

