package use_case.top_headlines;

import entity.UserPreferences;

public class TopHeadlinesInputData {

    private final UserPreferences userPreferences;

    public TopHeadlinesInputData(String requestType, UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public UserPreferences getUserPreferences() { return userPreferences; }
}
