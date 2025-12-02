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
        final String username = inputData.getUsername();

        if (username == null) {
            presenter.prepareFailView("You need to login first");
        }
        else if (dataAccessObject.get(username) == null) {
            presenter.prepareFailView("You need to login first");
        }
        else {
            presenter.initPreferenceView(dataAccessObject.get(username).getUserPreferences());
        }
    }

    @Override
    public void execute(SetPreferencesInputData inputData) {

        final String username = inputData.getUsername();
        final UserPreferences userPreferences = inputData.getUserPreferences();

        final User user = dataAccessObject.get(username);

        if (userPreferences.getLanguage() == null || userPreferences.getRegion() == null) {
            presenter.prepareFailView("Language and/or Region required.");
        }

        user.setUserPreferences(userPreferences);
        dataAccessObject.update(user);
        presenter.prepareSuccessView(new SetPreferencesOutputData(true, "Preferences Saved!",
                    username, userPreferences));
    }
}
