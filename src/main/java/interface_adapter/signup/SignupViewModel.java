package interface_adapter.signup;

import interface_adapter.ViewModel;

public class SignupViewModel extends ViewModel<SignupState> {

    public SignupViewModel() {
        super("signup");
        setState(new SignupState());
    }
}
