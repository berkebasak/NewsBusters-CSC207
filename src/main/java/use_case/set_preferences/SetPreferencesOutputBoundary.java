package use_case.set_preferences;

import entity.UserPreferences;

public interface SetPreferencesOutputBoundary {
    void initPreferenceView(UserPreferences userPreferences);
    void prepareSuccessView(SetPreferencesOutputData outputData);
    void prepareFailView(String message);
}
