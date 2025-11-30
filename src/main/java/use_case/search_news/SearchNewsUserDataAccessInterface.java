package use_case.search_news;

import java.util.List;
import entity.Article;
import entity.UserPreferences;

/**
 * DAO interface for Searching News by Keyword Use Case.
 * Gets articles from a data source
 */
public interface SearchNewsUserDataAccessInterface {

    /**
     * Searches for articles by keyword.
     * @param keyword keyword to search for
     */
    List<Article> searchByKeyword(String keyword, UserPreferences userPreferences);
}
