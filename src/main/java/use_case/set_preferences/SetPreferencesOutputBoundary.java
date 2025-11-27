package use_case.set_preferences;

public interface SetPreferencesOutputBoundary {
    void prepareSuccessView(SetPreferencesOutputData outputData);
    void prepareFailView(String message);
}
