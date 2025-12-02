package use_case.save_article;

/**
 * The {@code SaveArticleInputBoundary} defines the input method for the
 * Save Article use case.
 *
 * <p>
 * This boundary is called by the controller when the user attempts to save an
 * article, and signals the interactor to perform the business logic required
 * to process the request.
 */
public interface SaveArticleInputBoundary {

    /**
     * Executes the Save Article use case with the given input data.
     *
     * @param inputData the data required to save an article, such as username and article details
     */
    void execute(SaveArticleInputData inputData);
}

