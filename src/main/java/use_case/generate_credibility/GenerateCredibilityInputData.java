package use_case.generate_credibility;

import entity.Article;

import java.util.List;

public class GenerateCredibilityInputData {
    private final List<Article> articles;

    public GenerateCredibilityInputData(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
