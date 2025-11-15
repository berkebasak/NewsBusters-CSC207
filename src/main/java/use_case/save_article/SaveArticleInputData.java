package use_case.save_article;

import entity.Article;

public class SaveArticleInputData {
    private final Article article;

    public SaveArticleInputData(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}

