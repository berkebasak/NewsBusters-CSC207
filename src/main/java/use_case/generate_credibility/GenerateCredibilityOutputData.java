package use_case.generate_credibility;

import entity.Article;

import java.util.List;

public class GenerateCredibilityOutputData {
    private final List<Article> articles;
    public GenerateCredibilityOutputData(List<Article> articles) {
        this.articles = articles;
    }
    public List<Article> getArticles() {
        return articles;
    }
}
