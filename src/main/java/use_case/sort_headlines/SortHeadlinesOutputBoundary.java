package use_case.sort_headlines;

public interface SortHeadlinesOutputBoundary {
    void prepareSuccessView(SortHeadlinesOutputData outputData);

    void prepareFailView(String error);
}
