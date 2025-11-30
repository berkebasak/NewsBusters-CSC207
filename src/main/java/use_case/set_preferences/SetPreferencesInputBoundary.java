package use_case.set_preferences;

import entity.UserPreferences;

public interface SetPreferencesInputBoundary {
    void load(SetPreferencesInputData inputData);
    void execute(SetPreferencesInputData inputData);
}
