package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary interactor;

    public SignupController(SignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void signup(String username, String password, String confirmPassword) {
        SignupInputData inputData = new SignupInputData(username, password, confirmPassword);
        interactor.execute(inputData);
    }
}
