package use_case.load_saved_articles;

import entity.User;
import data_access.UserDataAccessInterface;

public class LoadSavedArticlesInteractor implements LoadSavedArticlesInputBoundary{

    private final UserDataAccessInterface userDataAccess;
    private final LoadSavedArticlesOutputBoundary presenter;

    public LoadSavedArticlesInteractor(UserDataAccessInterface userDataAccess,
                                       LoadSavedArticlesOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
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

        presenter.prepareSuccessView(
                new LoadSavedArticlesOutputData(user.getUsername(),
                        user.getSavedArticles())
        );
    }
}
