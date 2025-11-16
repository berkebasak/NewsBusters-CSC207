package use_case.discover_page;

public interface DiscoverPageOutputBoundary {
    void prepareSuccessView(DiscoverPageOutputData response);

    void prepareNoHistoryView(String message);

    void prepareNoArticlesView(String message);
}
