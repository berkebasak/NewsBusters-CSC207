package interface_adapter.sort_headlines;

import entity.Article;
import use_case.sort_headlines.SortHeadlinesInputBoundary;
import use_case.sort_headlines.SortHeadlinesInputData;
import java.util.List;

public class SortHeadlinesController {
    private final SortHeadlinesInputBoundary interactor;

    public SortHeadlinesController(SortHeadlinesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String sortOrder, List<Article> articles) {
        SortHeadlinesInputData inputData = new SortHeadlinesInputData(sortOrder, articles);
        interactor.execute(inputData);
    }
}
