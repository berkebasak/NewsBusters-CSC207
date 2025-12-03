package use_case.set_preferences;

import entity.UserPreferences;

public class SetPreferencesInputData {
    private String username;
    private UserPreferences userPreferences;

    public SetPreferencesInputData(String username) {
        this.username = username;
    }

    public SetPreferencesInputData(String username, UserPreferences userPreferences) {
        this.username = username;
        this.userPreferences = userPreferences;
    }

    public String getUsername() {
        return username;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }
}
