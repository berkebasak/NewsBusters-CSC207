package use_case.view_credibility;

import entity.Article;

public class ViewCredibilityDetailsInputData {
    private final Article article;

    public ViewCredibilityDetailsInputData(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}
