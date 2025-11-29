package interface_adapter.set_preferences;

import entity.User;
import entity.UserPreferences;

import java.util.ArrayList;

public class SetPreferencesState {
    private String username;
    private UserPreferences userPreferences;
    private String error;
    private String message;

    public String getUsername() { return username; }

    public void  setUsername(String user) { this.username = username; }

    public UserPreferences getUserPreferences() { return userPreferences; }

    public void setUserPreferences(UserPreferences userPreferences) { this.userPreferences = userPreferences; }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
