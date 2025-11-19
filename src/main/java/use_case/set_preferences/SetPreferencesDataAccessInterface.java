package use_case.set_preferences;

import entity.UserPreferences;

public interface SetPreferencesDataAccessInterface {
    void save(UserPreferences userPreferences) throws Exception;
}
