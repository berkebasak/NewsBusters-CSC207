package use_case.search_news;

import entity.UserPreferences;

/**
 * The Input Data for the Search News by Keyword Use Case.
 * Holds the keyword entered by the user for searching articles.
 */
public class SearchNewsInputData {

    private final String keyword;
    private final UserPreferences userPreferences;

    /**
     * Creates a new SearchNewsInputData object, input data for the search.
     * @param keyword the keyword entered by the user
     * @param userPreferences the preference of the user
     */
    public SearchNewsInputData(String keyword, UserPreferences userPreferences) {
        this.keyword = keyword;
        this.userPreferences = userPreferences;
    }

    /**
     * Gets the keyword entered by the user.
     * @return the keyword as a String
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the userPreferece entered by the user.
     * @return the userPreference
     */
    public UserPreferences getUserPreferences() { return userPreferences; }
}
