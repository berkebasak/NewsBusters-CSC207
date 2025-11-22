package use_case.generate_credibility;

import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateCredibilityInteractorTest {

    static class FakeDAOGenerate implements GenerateCredibilityDataAccessInterface {

        @Override
        public Article enrichArticleContent(Article article) {
            if (article.getContent() == null || article.getContent().isBlank()) {
                article.setContent(article.getTitle());
            }
            return article;
        }

        @Override
        public CredibilityScore generateScore(Article article) {
            double sourceScore = 0.8;
            double sentimentScore = 0.6;
            double claimConfidence = 0.7;
            return new CredibilityScore(sourceScore, sentimentScore, claimConfidence);
        }
    }

    static class CapturingPresenter implements GenerateCredibilityOutputBoundary {
        GenerateCredibilityOutputData lastData;
        String lastError;

        @Override
        public void prepareSuccessView(GenerateCredibilityOutputData outputData) {
            this.lastData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
        }
    }

    @Test
    void generatesCredibilityForSingleArticle() {
        FakeDAOGenerate dao = new FakeDAOGenerate();
        CapturingPresenter presenter = new CapturingPresenter();
        GenerateCredibilityInputBoundary interactor =
                new GenerateCredibilityInteractor(dao, presenter);

        Article article = new Article();
        article.setTitle("Colorado Invests $5.6 Million in Work-Based Learning to Enhance Higher Education in Rural Areas");
        article.setSource("hoodline");
        article.setUrl("https://hoodline.com/2025/11/colorado-invests-5-6-million-in-work-based-learning-to-enhance-higher-education-in-rural-areas/");

        GenerateCredibilityInputData inputData =
                new GenerateCredibilityInputData(Collections.singletonList(article));

        interactor.execute(inputData);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastData);

        List<Article> result = presenter.lastData.getArticles();
        assertNotNull(result);
        assertEquals(1, result.size());

        Article enriched = result.get(0);
        assertEquals("hoodline", enriched.getSource());
        assertEquals(
                "Colorado Invests $5.6 Million in Work-Based Learning to Enhance Higher Education in Rural Areas",
                enriched.getTitle()
        );
        assertNotNull(enriched.getCredibilityScore());
        assertTrue(enriched.getTrustScore() >= 0.0 && enriched.getTrustScore() <= 1.0);
        assertNotNull(enriched.getConfidenceLevel());
    }
}
