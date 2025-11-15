package use_case.save_article;

import entity.Article;

public class SaveArticleInteractor implements SaveArticleInputBoundary{

    private final SaveArticleDataAccessInterface dataAccess;
    private final SaveArticleOutputBoundary presenter;

    public SaveArticleInteractor(SaveArticleDataAccessInterface dataAccess,
                                 SaveArticleOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveArticleInputData inputData) {
        Article article = inputData.getArticle();

        if (article == null) {
            presenter.present(new SaveArticleOutputData(false,
                    "You need to select an article first."));
            return;
        }

        try{
            if ((article.getId() != null && dataAccess.existsById(article.getId())) ||
                    (article.getUrl() != null && dataAccess.existsByUrl(article.getUrl()))) {

                presenter.present(new SaveArticleOutputData(false,
                        "Already saved."));
                return;
            }

            dataAccess.save(article);
            presenter.present(new SaveArticleOutputData(true,
                    "Saved."));

        } catch (Exception e){
            presenter.present(new SaveArticleOutputData(false,
                    "Could not save."));
        }
    }
}
