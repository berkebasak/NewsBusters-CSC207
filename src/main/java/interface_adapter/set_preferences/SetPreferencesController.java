package interface_adapter.set_preferences;

import entity.UserPreferences;
import use_case.set_preferences.SetPreferencesInputBoundary;
import use_case.set_preferences.SetPreferencesInputData;

public class SetPreferencesController {
    private final SetPreferencesInputBoundary setPreferencesInteractor;

    public SetPreferencesController(SetPreferencesInputBoundary setPreferencesInteractor) {
        this.setPreferencesInteractor = setPreferencesInteractor;
    }

    public void load(String username) {
        SetPreferencesInputData inputData = new SetPreferencesInputData(username);
        setPreferencesInteractor.load(inputData);
    }

    public void save(String username, UserPreferences userPreferences) {
        SetPreferencesInputData inputData = new SetPreferencesInputData(username, userPreferences);
        setPreferencesInteractor.execute(inputData);
    }
}
