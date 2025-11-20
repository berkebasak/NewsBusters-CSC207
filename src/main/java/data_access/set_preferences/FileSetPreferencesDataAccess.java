package data_access.set_preferences;

import entity.User;
import entity.UserPreferences;
import use_case.set_preferences.SetPreferencesDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileSetPreferencesDataAccess implements SetPreferencesDataAccessInterface {

    private final File storage;
    private final Map<String, User> users = new HashMap<>();

    public FileSetPreferencesDataAccess(String filePath) throws IOException {
        this.storage = new File(filePath);
        initializeStorage();
        loadUsers();
    }

    @Override
    public void save(UserPreferences userPreferences) throws Exception {

    }
}
