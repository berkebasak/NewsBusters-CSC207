package use_case.set_preferences;

import entity.User;
import entity.UserPreferences;

public interface SetPreferencesDataAccessInterface {
    void savePreferences(String username, UserPreferences userPreferences) throws Exception;
}
