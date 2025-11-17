package use_case.login;

import entity.User;

public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccess,
                           LoginOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        String username = inputData.getUsername() == null ? "" : inputData.getUsername().trim();
        String password = inputData.getPassword() == null ? "" : inputData.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            presenter.prepareFailView("Please enter both username and password.");
            return;
        }

        if (!userDataAccess.existsByName(username)) {
            presenter.prepareFailView("Account not found.");
            return;
        }

        User user = userDataAccess.get(username);
        if (user == null || !user.passwordMatches(password)) {
            presenter.prepareFailView("Incorrect password.");
            return;
        }

        presenter.prepareSuccessView(new LoginOutputData(user.getUsername(), user.getSavedArticles()));
    }
}
