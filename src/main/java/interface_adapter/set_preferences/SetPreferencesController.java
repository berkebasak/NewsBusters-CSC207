package interface_adapter.set_preferences;

import entity.User;
import entity.UserPreferences;
import use_case.set_preferences.SetPreferencesInputBoundary;
import use_case.set_preferences.SetPreferencesInputData;

public class SetPreferencesController {
    private final SetPreferencesInputBoundary setPreferencesInteractor;

    public SetPreferencesController(SetPreferencesInputBoundary setPreferencesInteractor) {
        this.setPreferencesInteractor = setPreferencesInteractor;
    }

    public void save(String username, UserPreferences userPreferences) {
        SetPreferencesInputData inputData = new SetPreferencesInputData(username, userPreferences);
        setPreferencesInteractor.execute(inputData);
    }
}
