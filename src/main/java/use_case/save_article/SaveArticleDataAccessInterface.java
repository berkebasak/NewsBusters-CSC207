package use_case.save_article;

import entity.Article;

public interface SaveArticleDataAccessInterface {
    boolean existsById(String id);
    boolean existsByUrl(String url);

    void save(Article article) throws Exception;
}