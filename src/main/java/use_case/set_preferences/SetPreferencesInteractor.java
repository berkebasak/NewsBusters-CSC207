package use_case.set_preferences;

import entity.User;
import entity.UserPreferences;

public class SetPreferencesInteractor implements SetPreferencesInputBoundary {

    private final SetPreferencesDataAccessInterface dataAccessObject;
    private final SetPreferencesOutputBoundary presenter;

    public SetPreferencesInteractor(SetPreferencesDataAccessInterface dataAccessObject,
                                    SetPreferencesOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void load(SetPreferencesInputData inputData) {
        String username = inputData.getUsername();

        if (username == null) {
            presenter.prepareFailView("You need to login first");
            return;
        }

        User user = dataAccessObject.get(username);
        if (user == null) {
            presenter.prepareFailView("You need to login first");
            return;
        }

        presenter.initPreferenceView(user.getUserPreferences());
    }

    @Override
    public void execute(SetPreferencesInputData inputData) {

        String username = inputData.getUsername();
        UserPreferences userPreferences = inputData.getUserPreferences();

        if (username == null) {
            presenter.prepareFailView("You need to login first");
            return;
        }

        User user = dataAccessObject.get(username);
        if (user == null) {
            presenter.prepareFailView("You need to login first");
            return;
        }

        if (userPreferences == null) {
            presenter.prepareFailView("No user preferences found.");
            return;
        }

        if (userPreferences.getLanguage() == null || userPreferences.getRegion() == null) {
            presenter.prepareFailView("Language and/or Region required.");
        }

        user.setUserPreferences(userPreferences);
        dataAccessObject.update(user);
        presenter.prepareSuccessView(new SetPreferencesOutputData(true, "Preferences Saved!",
                    username, userPreferences));
    }
}
