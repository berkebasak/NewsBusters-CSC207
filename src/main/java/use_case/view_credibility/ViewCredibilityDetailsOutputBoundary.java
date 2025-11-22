package use_case.view_credibility;

public interface ViewCredibilityDetailsOutputBoundary {
    void prepareSuccessView(ViewCredibilityDetailsOutputData outputData);
    void prepareFailView(String message);
}
