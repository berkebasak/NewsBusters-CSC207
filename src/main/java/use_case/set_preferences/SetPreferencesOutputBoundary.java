package use_case.set_preferences;

import entity.UserPreferences;

public interface SetPreferencesOutputBoundary {
    /**
     * Initializes preference view after login.
     * @param userPreferences the preferences that need to be initialized, taken from database.
     */
    void initPreferenceView(UserPreferences userPreferences);

    /**
     * Called when preferences are saved successfully.
     * @param outputData data to be used for further processing after successful save.
     */
    void prepareSuccessView(SetPreferencesOutputData outputData);

    /**
     * Called when preferences failed to save.
     * @param message message for the user on what went wrong.
     */
    void prepareFailView(String message);
}
