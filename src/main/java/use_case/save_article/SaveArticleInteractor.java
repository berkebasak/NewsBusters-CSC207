package use_case.save_article;

import entity.Article;
import entity.User;
import data_access.FileUserDataAccessObject;
import interface_adapter.login.LoginViewModel;

public class SaveArticleInteractor implements SaveArticleInputBoundary{

    private final SaveArticleDataAccessInterface dataAccess;
    private final SaveArticleOutputBoundary presenter;
    private final FileUserDataAccessObject userDataAccess;
    private final LoginViewModel loginViewModel;

    public SaveArticleInteractor(SaveArticleDataAccessInterface dataAccess,
                                 SaveArticleOutputBoundary presenter,
                                 FileUserDataAccessObject userDataAccess,
                                 LoginViewModel loginViewModel) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.userDataAccess = userDataAccess;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void execute(SaveArticleInputData inputData) {
        Article article = inputData.getArticle();

        if (article == null) {
            presenter.present(new SaveArticleOutputData(false,
                    "You need to select an article first."));
            return;
        }

        // basic guard to see if currUser is null
        String username = loginViewModel.getState().getUsername();
        if (username == null || username.isBlank()) {
            presenter.present(new SaveArticleOutputData(false,
                    "Could not save. No logged-in user."));
            return;
        }

        User currUser = userDataAccess.get(username);
        if (currUser == null) {
            presenter.present(new SaveArticleOutputData(false,
                    "Could not save. User not found."));
            return;
        }

        try{
            if (article.getUrl() != null && dataAccess.existsByUserandUrl(username,
                    article.getUrl())) {
                presenter.present(new SaveArticleOutputData(false,
                        "Already saved."));
                return;
            }

            // save to txt file
            dataAccess.saveForUser(username, article);
            // update domain object
            currUser.addSavedArticle(article);
            // save to json
            userDataAccess.update(currUser);

            presenter.present(new SaveArticleOutputData(true,
                    "Saved."));

        } catch (Exception e){
            e.printStackTrace();
            presenter.present(new SaveArticleOutputData(false,
                    "Could not save."));
        }
    }
}
