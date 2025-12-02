package use_case.save_article;

import entity.Article;

/**
 * The {@code SaveArticleDataAccessInterface} defines the methods required for
 * persisting saved articles and checking their existence in the underlying data source.
 *
 * <p>
 * Implementations of this interface are responsible for interacting with storage
 * (e.g., in-memory, file-based, or database) and providing persistence support
 * for the Save Article use case.
 */
public interface SaveArticleDataAccessInterface {

    /**
     * Checks whether a saved article already exists for the given user and URL.
     *
     * @param username the username of the user whose saved articles are being queried
     * @param url      the article URL to check for
     * @return {@code true} if the article is already stored for that user,
     *         {@code false} otherwise
     */
    boolean existsByUserandUrl(String username, String url);

    /**
     * Saves an article for the specified user.
     *
     * @param username the username for whom the article should be saved
     * @param article  the article to store
     * @throws Exception if an error occurs while writing to the storage layer
     */
    void saveForUser(String username, Article article) throws Exception;
}
