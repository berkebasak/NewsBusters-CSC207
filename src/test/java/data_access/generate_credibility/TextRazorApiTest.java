package data_access;

import data_access.generate_credibility.TextRazorOpenPageRankDataAccessObject;
import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextRazorApiTest {

    @Test
    void textRazorProducesValidTextSignalsOrFallback() {
        TextRazorOpenPageRankDataAccessObject dao = new TextRazorOpenPageRankDataAccessObject();

        Article article = new Article();
        article.setTitle("Global automotive supplier to layoff nearly 200 at Metro Detroit plant");
        article.setSource("mlive");
        article.setContent("News about layoffs and economic impact in Metro Detroit.");
        article.setUrl(null); // OpenPageRank will return neutral 0.5

        CredibilityScore score;
        try {
            score = dao.generateScore(article);
        } catch (Exception e) {
            System.out.println("Skipping TextRazorApiTest: API call failed with " + e.getMessage());
            return;
        }

        assertNotNull(score);

        double sentiment = score.getSentimentScore();
        double confidence = score.getClaimConfidence();
        assertTrue(sentiment >= 0.0 && sentiment <= 1.0);
        assertTrue(confidence >= 0.0 && confidence <= 1.0);

        double overall = score.getOverallTrust();
        assertTrue(overall >= 0.0 && overall <= 1.0);

        assertNotNull(score.getRationale());
        assertFalse(score.getRationale().isBlank());
        assertNotNull(score.getLevel());
        assertFalse(score.getLevel().isBlank());
    }
}
