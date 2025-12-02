package use_case.top_headlines;

import entity.UserPreferences;

public class TopHeadlinesInputData {

    private final String requestType;
    private final UserPreferences userPreferences;

    public TopHeadlinesInputData(String requestType, UserPreferences userPreferences) {
        this.requestType = requestType;
        this.userPreferences = userPreferences;
    }

    public String getRequestType() {
        return requestType;
    }

    public UserPreferences getUserPreferences() { return userPreferences; }
}
