package interface_adapter.set_preferences;

import entity.UserPreferences;
import use_case.set_preferences.SetPreferencesInputBoundary;
import use_case.set_preferences.SetPreferencesInputData;

public class SetPreferencesController {
    private final SetPreferencesInputBoundary setPreferencesInteractor;

    public SetPreferencesController(SetPreferencesInputBoundary setPreferencesInteractor) {
        this.setPreferencesInteractor = setPreferencesInteractor;
    }

    /**
     * Loads the initial preferences data for view.
     * @param username username of the user.
     */
    public void load(String username) {
        final SetPreferencesInputData inputData = new SetPreferencesInputData(username);
        setPreferencesInteractor.load(inputData);
    }

    /**
     * Saves userPreferences to user when he clicks the save button.
     * @param username username of the user.
     * @param userPreferences the user's preferences.
     */
    public void save(String username, UserPreferences userPreferences) {
        final SetPreferencesInputData inputData = new SetPreferencesInputData(username, userPreferences);
        setPreferencesInteractor.execute(inputData);
    }
}
