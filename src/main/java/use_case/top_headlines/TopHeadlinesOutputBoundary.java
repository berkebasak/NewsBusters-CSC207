package use_case.top_headlines;

public interface TopHeadlinesOutputBoundary {
    void present(TopHeadlinesOutputData outputData);
    void presentAlternative(TopHeadlinesOutputData outputData, String message);

}
