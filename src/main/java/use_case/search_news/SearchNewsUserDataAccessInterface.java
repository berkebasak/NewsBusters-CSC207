package use_case.search_news;

import java.util.List;
import entity.Article;

/**
 * DAO interface for Searching News by Keyword Use Case.
 */
public interface SearchNewsUserDataAccessInterface {
    List<Article> searchByKeyword(String keyword) throws Exception;
}
