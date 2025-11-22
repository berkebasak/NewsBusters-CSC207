package use_case.view_credibility;

import entity.Article;
import entity.CredibilityScore;

public class ViewCredibilityDetailsInteractor implements ViewCredibilityDetailsInputBoundary {

    private final ViewCredibilityDetailsOutputBoundary presenter;

    private static final double WEIGHT_SOURCE = 0.7;
    private static final double WEIGHT_SENTIMENT = 0.15;
    private static final double WEIGHT_CLAIM = 0.15;

    public ViewCredibilityDetailsInteractor(ViewCredibilityDetailsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCredibilityDetailsInputData inputData) {
        Article article = inputData.getArticle();
        if (article == null) {
            presenter.prepareFailView("No article selected.");
            return;
        }

        CredibilityScore score = article.getCredibilityScore();
        if (score == null) {
            presenter.prepareFailView("Credibility score not generated yet.");
            return;
        }

        ViewCredibilityDetailsOutputData outputData =
                new ViewCredibilityDetailsOutputData(
                        article.getTitle(),
                        article.getSource(),
                        article.getUrl(),
                        score.getSourceScore(),
                        score.getSentimentScore(),
                        score.getClaimConfidence(),
                        score.getOverallTrust(),
                        score.getLevel(),
                        score.getRationale(),
                        WEIGHT_SOURCE,
                        WEIGHT_SENTIMENT,
                        WEIGHT_CLAIM
                );

        presenter.prepareSuccessView(outputData);
    }
}
