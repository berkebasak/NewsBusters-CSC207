package interface_adapter.generate_credibility;

import entity.Article;
import use_case.generate_credibility.GenerateCredibilityInputBoundary;
import use_case.generate_credibility.GenerateCredibilityInputData;

import java.util.ArrayList;
import java.util.List;

public class GenerateCredibilityController {

    private final GenerateCredibilityInputBoundary interactor;

    public GenerateCredibilityController(GenerateCredibilityInputBoundary interactor) {
        this.interactor = interactor;
    }

    /*
      Generate credibility score for a single article.
     */
    public void generateForArticle(Article article) {
        if (article == null) {
            return;
        }
        List<Article> one = new ArrayList<>();
        one.add(article);
        GenerateCredibilityInputData input = new GenerateCredibilityInputData(one);
        interactor.execute(input);
    }

    /*
      Generate credibility score for all articles in the given list.
     */
    public void generateForAll(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return;
        }
        GenerateCredibilityInputData input = new GenerateCredibilityInputData(articles);
        interactor.execute(input);
    }
}
