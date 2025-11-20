package use_case.profile;

import data_access.UserDataAccessInterface;
import entity.Article;
import entity.User;

public class ProfileInteractor implements ProfileInputBoundary {
    private final UserDataAccessInterface userDataAccessObject;
    private final ProfileOutputBoundary profilePresenter;

    public ProfileInteractor(UserDataAccessInterface userDataAccessObject,
            ProfileOutputBoundary profilePresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.profilePresenter = profilePresenter;
    }

    @Override
    public void execute(ProfileInputData inputData) {
        String username = inputData.getUsername();
        User user = userDataAccessObject.get(username);

        if (user == null) {
            profilePresenter.prepareFailView("User not found.");
        } else {
            ProfileOutputData outputData = new ProfileOutputData(user.getUsername(), user.getHistory());
            profilePresenter.prepareSuccessView(outputData);
        }
    }

    @Override
    public void addHistory(String username, Article article) {
        User user = userDataAccessObject.get(username);
        if (user != null) {
            user.addToHistory(article);
            userDataAccessObject.update(user);
        }
    }
}
