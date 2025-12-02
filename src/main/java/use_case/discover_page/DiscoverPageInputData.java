package use_case.discover_page;

import entity.UserPreferences;

import java.util.Set;

public class DiscoverPageInputData {
    private final Set<String> currentTopics;
    private final int currentPage;
    private final String username;
    private final UserPreferences userPreferences;

    public DiscoverPageInputData(Set<String> currentTopics, int currentPage, String username, UserPreferences userPreferences) {
        this.currentTopics = currentTopics;
        this.currentPage = currentPage;
        this.username = username;
        this.userPreferences = userPreferences;
    }

    public Set<String> getCurrentTopics() {
        return currentTopics;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getUsername() {
        return username;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }
}
