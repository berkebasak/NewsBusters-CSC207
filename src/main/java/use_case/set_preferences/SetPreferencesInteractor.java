package use_case.set_preferences;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserPreferences;

import java.io.File;

public class SetPreferencesInteractor implements SetPreferencesInputBoundary {

    private final FileUserDataAccessObject fileUserDataAccessObject;
    private final SetPreferencesDataAccessInterface dataAccessObject;
    private final SetPreferencesOutputBoundary presenter;

    public SetPreferencesInteractor(SetPreferencesDataAccessInterface dataAccessObject,
                                    SetPreferencesOutputBoundary presenter, FileUserDataAccessObject fileUserDataAccessObject) {
        this.fileUserDataAccessObject = fileUserDataAccessObject;
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(SetPreferencesInputData inputData) {

        String username = inputData.getUsername();
        UserPreferences userPreferences = inputData.getUserPreferences();

        if (username == null) {
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

        try {
            dataAccessObject.savePreferences(username, userPreferences);
            presenter.prepareSuccessView(
                    new SetPreferencesOutputData(true, "Preferences Saved!",
                            username, userPreferences));
        } catch (Exception e) {
            User user = fileUserDataAccessObject.get(username);
            presenter.prepareSuccessView(
                    new SetPreferencesOutputData(false, "Could not save preferences!",
                            username, user.getUserPreferences()));
        }
    }
}
