package use_case.filter_news;

/**
 * Input Boundary for the Filter News use case.
 */
public interface FilterNewsInputBoundary {

    /**
     * Executes the filter operation.
     * @param inputData the user's selected topics
     */
    void execute(FilterNewsInputData inputData);
}
