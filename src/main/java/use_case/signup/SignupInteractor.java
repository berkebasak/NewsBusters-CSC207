package use_case.signup;

import entity.User;

public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary presenter;

    public SignupInteractor(SignupUserDataAccessInterface userDataAccess,
                             SignupOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        String username = inputData.getUsername() == null ? "" : inputData.getUsername().trim();
        String password = inputData.getPassword() == null ? "" : inputData.getPassword();
        String confirmPassword = inputData.getConfirmPassword() == null ? "" : inputData.getConfirmPassword();

        if (username.isEmpty()) {
            presenter.prepareFailView("Username cannot be empty.");
            return;
        }
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            presenter.prepareFailView("Password fields cannot be empty.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            presenter.prepareFailView("Passwords do not match.");
            return;
        }

        if (userDataAccess.existsByName(username)) {
            presenter.prepareFailView("An account with that username already exists.");
            return;
        }

        try {
            User user = User.create(username, password);
            userDataAccess.save(user);
            presenter.prepareSuccessView(new SignupOutputData(user.getUsername()));
        } catch (Exception e) {
            presenter.prepareFailView("Could not create account. " + e.getMessage());
        }
    }
}
