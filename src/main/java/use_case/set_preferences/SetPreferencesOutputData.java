package use_case.set_preferences;

import entity.UserPreferences;

public class SetPreferencesOutputData {
    private final String username;
    private final UserPreferences userPreferences;
    private final boolean success;
    private final String message;

    public SetPreferencesOutputData(boolean success, String message,
                                    String username, UserPreferences userPreferences) {
        this.username = username;
        this.userPreferences = userPreferences;
        this.success = success;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
