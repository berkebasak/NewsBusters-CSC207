package use_case.set_preferences;

public interface SetPreferencesInputBoundary {
    /**
     * Loads initial preferences view based on saved data.
     * @param inputData taken from the database after initial login.
     */
    void load(SetPreferencesInputData inputData);

    /**
     * Saves preferences based on user input.
     * @param inputData taken from user input.
     */
    void execute(SetPreferencesInputData inputData);
}
