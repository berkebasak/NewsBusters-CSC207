package use_case.search_news;

/**
 * The Input Data for the Search News by Keyword Use Case.
 * Holds the keyword entered by the user for searching articles.
 */
public class SearchNewsInputData {

    private final String keyword;

    /**
     * Creates a new SearchNewsInputData object, input data for the search.
     * @param keyword the keyword entered by the user
     */
    public SearchNewsInputData(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets the keyword entered by the user.
     * @return the keyword as a String
     */
    public String getKeyword() {
        return keyword;
    }
}
