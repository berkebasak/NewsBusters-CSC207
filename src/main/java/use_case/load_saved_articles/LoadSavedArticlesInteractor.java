package use_case.load_saved_articles;

import entity.Article;
import entity.User;
import data_access.UserDataAccessInterface;
import use_case.save_article.SaveArticleDataAccessInterface;

import java.util.List;

public class LoadSavedArticlesInteractor implements LoadSavedArticlesInputBoundary{

    private final UserDataAccessInterface userDataAccess;
    private final LoadSavedArticlesOutputBoundary presenter;
    private final SaveArticleDataAccessInterface savedTxtDataAccess; //optional txt check

    public LoadSavedArticlesInteractor(UserDataAccessInterface userDataAccess,
                                       LoadSavedArticlesOutputBoundary presenter,
                                       SaveArticleDataAccessInterface savedTxtDataAccess) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
        this.savedTxtDataAccess = savedTxtDataAccess;
    }

    @Override
    public void execute(LoadSavedArticlesInputData inputData) {
        String username = inputData.getUsername();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("No logged-in user.");
            return;
        }

        User user = userDataAccess.get(username);
        if (user == null) {
            presenter.prepareFailView("User not found.");
            return;
        }

        List<Article> saved = user.getSavedArticles();

        //optional: coherence check between users.json and saved_articles.txt
        if (savedTxtDataAccess != null) {
            for (Article a : saved) {
                if (a.getUrl() != null &&
                        !savedTxtDataAccess.existsByUserandUrl(username, a.getUrl())) {
                    System.out.println("Warning: users.json jas an article not in saved_articles.txt:"
                    + a.getUrl());
                }
            }
        }

        presenter.prepareSuccessView(
                new LoadSavedArticlesOutputData(user.getUsername(),
                        user.getSavedArticles())
        );
    }
}
