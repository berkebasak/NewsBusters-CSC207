package use_case.load_saved_articles;

public interface LoadSavedArticlesOutputBoundary {
    void prepareSuccessView(LoadSavedArticlesOutputData outputData);

    void prepareFailView(String errorMessage);
}
