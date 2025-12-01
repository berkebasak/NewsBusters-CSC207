package interface_adapter.save_article;

import entity.Article;
import use_case.save_article.SaveArticleInputBoundary;
import use_case.save_article.SaveArticleInputData;

/**
 * Controller for saving articles.
 */
public class SaveArticleController {

    private final SaveArticleInputBoundary saveArticleInteractor;

    /**
     * Constructs a controller for saving articles.
     *
     * @param saveArticleInteractor the use case interactor
     */
    public SaveArticleController(SaveArticleInputBoundary saveArticleInteractor) {
        this.saveArticleInteractor = saveArticleInteractor;
    }

    /**
     * Saves the given article using the save-article use case.
     *
     * @param article the article to be saved
     */
    public void save(Article article) {
        final SaveArticleInputData inputData = new SaveArticleInputData(article);
        saveArticleInteractor.execute(inputData);
    }
}
