package use_case.filter_credibility;

/**
 * Interface for the filter credibility use case interactor.
 * Defines the contract for filtering articles by trust score level.
 */
public interface FilterCredibilityInputBoundary {
    /**
     * Executes the filter credibility use case.
     * 
     * @param inputData the input data containing articles and filter level
     */
    void execute(FilterCredibilityInputData inputData);
}
