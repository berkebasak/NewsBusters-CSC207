package use_case.view_credibility;

import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewCredibilityDetailsInteractorTest {

    static class CapturingPresenter implements ViewCredibilityDetailsOutputBoundary {
        ViewCredibilityDetailsOutputData lastData;
        String lastError;

        @Override
        public void prepareSuccessView(ViewCredibilityDetailsOutputData outputData) {
            this.lastData = outputData;
        }

        @Override
        public void prepareFailView(String message) {
            this.lastError = message;
        }
    }

    @Test
    void buildsDetailsFromArticleWithCredibilityScore() {
        Article article = new Article();
        article.setTitle("Colorado Invests $5.6 Million in Work-Based Learning");
        article.setSource("hoodline");
        article.setUrl("https://hoodline.com/2025/11/colorado-invests-5-6-million-in-work-based-learning");

        CredibilityScore score = new CredibilityScore(
                0.7,   // sourceScore
                0.5,   // sentimentScore
                0.6    // claimConfidence
        );
        article.setCredibilityScore(score);
        article.setTrustScore(score.getOverallTrust());
        article.setConfidenceLevel(score.getLevel());

        CapturingPresenter presenter = new CapturingPresenter();
        ViewCredibilityDetailsInputBoundary interactor =
                new ViewCredibilityDetailsInteractor(presenter);

        ViewCredibilityDetailsInputData inputData =
                new ViewCredibilityDetailsInputData(article);

        interactor.execute(inputData);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastData);

        ViewCredibilityDetailsOutputData out = presenter.lastData;

        assertEquals(article.getTitle(), out.getTitle());
        assertEquals(article.getSource(), out.getSource());
        assertEquals(article.getUrl(), out.getUrl());

        assertEquals(score.getSourceScore(), out.getSourceScore(), 1e-9);
        assertEquals(score.getSentimentScore(), out.getSentimentScore(), 1e-9);
        assertEquals(score.getClaimConfidence(), out.getClaimConfidence(), 1e-9);
        assertEquals(score.getOverallTrust(), out.getOverallTrust(), 1e-9);
        assertEquals(score.getLevel(), out.getLevel());
        assertNotNull(out.getRationale());
        assertFalse(out.getRationale().isBlank());

        double wSource = out.getWeightSource();
        double wSent = out.getWeightSentiment();
        double wClaim = out.getWeightClaim();
        double sum = wSource + wSent + wClaim;

        assertTrue(wSource >= 0.0 && wSource <= 1.0);
        assertTrue(wSent >= 0.0 && wSent <= 1.0);
        assertTrue(wClaim >= 0.0 && wClaim <= 1.0);
        assertEquals(1.0, sum, 1e-9);
    }
}
