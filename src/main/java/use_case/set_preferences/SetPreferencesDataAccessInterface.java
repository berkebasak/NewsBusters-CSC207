package use_case.set_preferences;

import entity.User;

public interface SetPreferencesDataAccessInterface {
    /**
     * Updates user after to save his preferences.
     * @param user the user.
     */
    void update(User user);

    /**
     * Gets the user with username.
     * @param username username of the user.
     * @return user with username.
     */
    User get(String username);
}
