package interface_adapter.load_saved_articles;

import use_case.load_saved_articles.LoadSavedArticlesInputBoundary;
import use_case.load_saved_articles.LoadSavedArticlesInputData;

public class LoadSavedArticlesController {

    private final LoadSavedArticlesInputBoundary interactor;

    public LoadSavedArticlesController(LoadSavedArticlesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        LoadSavedArticlesInputData inputData = new LoadSavedArticlesInputData(username);
        interactor.execute(inputData);
    }
}
