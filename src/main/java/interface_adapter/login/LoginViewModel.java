package interface_adapter.login;

import interface_adapter.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {

    public static final String VIEW_NAME = "login";

    public LoginViewModel() {
        super(VIEW_NAME);
        setState(new LoginState());
    }
}
