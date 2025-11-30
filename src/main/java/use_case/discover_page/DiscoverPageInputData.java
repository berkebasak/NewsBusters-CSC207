package use_case.discover_page;

import entity.UserPreferences;

import java.util.Set;

public class DiscoverPageInputData {
    private final Set<String> currentTopics;
    private final int currentPage;
    private final UserPreferences userPreferences;

    public DiscoverPageInputData(Set<String> currentTopics, int currentPage, UserPreferences userPreferences) {
        this.currentTopics = currentTopics;
        this.currentPage = currentPage;
        this.userPreferences = userPreferences;
    }

    public Set<String> getCurrentTopics() {
        return currentTopics;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public UserPreferences getUserPreferences() { return userPreferences; }
}
