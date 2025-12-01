package use_case.save_article;

import data_access.FileUserDataAccessObject;
import entity.Article;
import entity.User;
import interface_adapter.login.LoginViewModel;

public class SaveArticleInteractor implements SaveArticleInputBoundary {

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
        SaveArticleOutputData outputData;

        final Article article = inputData.getArticle();

        if (article == null) {
            outputData = new SaveArticleOutputData(false,
                    "You need to select an article first.");
        }
        else {
            final String username = loginViewModel.getState().getUsername();

            if (username == null || username.isBlank()) {
                outputData = new SaveArticleOutputData(false,
                        "Could not save. No logged-in user.");
            }
            else {
                final User currUser = userDataAccess.get(username);

                if (currUser == null) {
                    outputData = new SaveArticleOutputData(false,
                            "Could not save. User not found.");
                }
                else {
                    try {
                        boolean alreadySaved = false;
                        if (article.getUrl() != null && dataAccess.existsByUserandUrl(
                                username, article.getUrl())) {
                            alreadySaved = true;
                        }

                        if (alreadySaved) {
                            outputData = new SaveArticleOutputData(false,
                                    "Already saved.");
                        }
                        else {
                            dataAccess.saveForUser(username, article);
                            currUser.addSavedArticle(article);
                            userDataAccess.update(currUser);

                            outputData = new SaveArticleOutputData(true,
                                    "Saved.");
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        outputData = new SaveArticleOutputData(false,
                                "Could not save.");
                    }
                }
            }
        }

        presenter.present(outputData);
    }
}
