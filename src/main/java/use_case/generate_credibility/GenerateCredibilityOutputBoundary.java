package use_case.generate_credibility;

public interface GenerateCredibilityOutputBoundary {
    void prepareSuccessView(GenerateCredibilityOutputData outputData);
    void prepareFailView(String message);
}
