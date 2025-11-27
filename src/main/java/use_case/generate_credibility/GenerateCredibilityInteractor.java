package use_case.generate_credibility;

import entity.Article;
import entity.CredibilityScore;

import java.util.ArrayList;
import java.util.List;

/**
 * UC5: Generate Credibility Score for each displayed article.
 */
public class GenerateCredibilityInteractor implements GenerateCredibilityInputBoundary {

    private final GenerateCredibilityDataAccessInterface signalsDAO;
    private final GenerateCredibilityOutputBoundary presenter;

    public GenerateCredibilityInteractor(GenerateCredibilityDataAccessInterface signalsDAO,
                                         GenerateCredibilityOutputBoundary presenter) {
        this.signalsDAO = signalsDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateCredibilityInputData inputData) {
        List<Article> articles = inputData.getArticles();
        if (articles == null || articles.isEmpty()) {
            presenter.prepareFailView("No articles to score.");
            return;
        }

        List<Article> updated = new ArrayList<>();

        try {
            for (Article article : articles) {
                try {
                    CredibilityScore score = signalsDAO.generateScore(article);

                    if (score == null) {
                        article.setCredibilityScore(null);
                        article.setTrustScore(0.0);
                        article.setConfidenceLevel("Insufficient data");
                    } else {
                        article.setCredibilityScore(score);
                        article.setTrustScore(score.getOverallTrust());
                        article.setConfidenceLevel(score.getLevel());
                    }

                    updated.add(article);
                } catch (Exception e) {
                    article.setCredibilityScore(null);
                    article.setTrustScore(0.0);
                    article.setConfidenceLevel("API_ERROR");
                    updated.add(article);
                }
            }

            presenter.prepareSuccessView(new GenerateCredibilityOutputData(updated));

        } catch (Exception e) {
            presenter.prepareFailView("Unable to generate credibility scores.");
        }
    }
}
