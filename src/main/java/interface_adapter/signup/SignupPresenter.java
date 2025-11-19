package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;

    public SignupPresenter(SignupViewModel signupViewModel,
                           ViewManagerModel viewManagerModel,
                           LoginViewModel loginViewModel) {
        this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        SignupState state = signupViewModel.getState();
        state.setError(null);
        state.setMessage("Account created for " + outputData.getUsername() + ". Please login.");
        signupViewModel.firePropertyChange("success");

        loginViewModel.firePropertyChange("reset");
        viewManagerModel.changeView(LoginViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SignupState state = signupViewModel.getState();
        state.setError(errorMessage);
        state.setMessage(null);
        signupViewModel.firePropertyChange("state");
    }
}
