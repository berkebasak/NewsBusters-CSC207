package use_case.set_preferences;

import entity.User;
import entity.UserPreferences;

public class SetPreferencesInputData {
    String username;
    UserPreferences userPreferences;

    public SetPreferencesInputData(String username, UserPreferences userPreferences) {
        this.username = username;
        this.userPreferences = userPreferences;
    }

    public String getUsername() { return username; }

    public UserPreferences getUserPreferences() { return userPreferences; }
}
