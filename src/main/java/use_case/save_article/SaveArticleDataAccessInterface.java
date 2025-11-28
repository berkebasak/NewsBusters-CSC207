package use_case.save_article;

import entity.Article;
import entity.User;

public interface SaveArticleDataAccessInterface {
    boolean existsByUserandUrl(String username, String url);

    void saveForUser(String username, Article article) throws Exception;
}