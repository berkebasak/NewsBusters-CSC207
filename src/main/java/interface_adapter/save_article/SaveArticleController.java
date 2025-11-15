package interface_adapter.save_article;

import entity.Article;
import use_case.save_article.SaveArticleInputBoundary;
import use_case.save_article.SaveArticleInputData;

public class SaveArticleController {

    private final SaveArticleInputBoundary saveArticleInteractor;

    public SaveArticleController(SaveArticleInputBoundary saveArticleInteractor) {
        this.saveArticleInteractor = saveArticleInteractor;
    }

    public void save(Article article) {
        SaveArticleInputData inputData = new SaveArticleInputData(article);
        saveArticleInteractor.execute(inputData);
    }
}
