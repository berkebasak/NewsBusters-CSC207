package use_case.generate_credibility;

import entity.Article;
import entity.CredibilityScore;


public interface GenerateCredibilityDataAccessInterface {


    Article enrichArticleContent(Article article) throws Exception;

    CredibilityScore generateScore(Article article) throws Exception;
}
