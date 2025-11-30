package use_case.set_preferences;

import entity.User;
import entity.UserPreferences;

public interface SetPreferencesDataAccessInterface {
    void save(User user);
    void update(User user);
    User get(String username);
}
