package use_case.discover_page;

import entity.Article;
import entity.UserPreferences;

import java.util.List;
import java.util.Set;

/**
 * Data Access Interface for the Discover Page use case.
 * Provides methods to access reading history and search for articles by topics.
 */
public interface DiscoverPageDataAccessInterface {

    /**
     * Gets all saved articles (reading history) from storage.
     * @return list of saved articles, or empty list if none exist
     */
    List<Article> getReadingHistory();

    /**
     * Extracts top topics from a list of articles based on word frequency in titles.
     * @param articles the articles to analyze
     * @param topN the number of top topics to return
     * @return set of top topic keywords
     */
    Set<String> extractTopTopics(List<Article> articles, int topN);

    /**
     * Searches for new articles related to the given topics.
     * @param topics the topics to search for
     * @param page the page number to fetch (0-based, where 0 is the first page)
     * @return list of articles related to the topics
     */
    List<Article> searchByTopics(Set<String> topics, int page, UserPreferences userPreferences);
}
