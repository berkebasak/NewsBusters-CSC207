package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(LoginViewModel loginViewModel,
                          ViewManagerModel viewManagerModel) {
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        LoginState state = loginViewModel.getState();
        state.setError(null);
        state.setUsername(outputData.getUsername());
        state.setMessage("Welcome back, " + outputData.getUsername() + "!");
        loginViewModel.setState(state);
        loginViewModel.firePropertyChange("state");
        int delayMillis = 700;

        new javax.swing.Timer(delayMillis, e -> {
            viewManagerModel.changeView(TopHeadlinesViewModel.VIEW_NAME);
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        LoginState state = loginViewModel.getState();
        state.setError(errorMessage);
        state.setMessage(null);
        loginViewModel.firePropertyChange("state");
    }
}
