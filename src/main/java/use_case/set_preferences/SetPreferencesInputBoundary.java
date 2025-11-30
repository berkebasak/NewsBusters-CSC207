package use_case.set_preferences;

public interface SetPreferencesInputBoundary {
    void load(SetPreferencesInputData inputData);
    void execute(SetPreferencesInputData inputData);
}
