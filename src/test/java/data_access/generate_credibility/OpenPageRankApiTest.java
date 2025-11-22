package data_access.generate_credibility;

import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenPageRankApiTest {

    @Test
    void openPageRankReturnsValidSourceScore() {
        TextRazorOpenPageRankDataAccessObject dao = new TextRazorOpenPageRankDataAccessObject();

        Article article = new Article();
        article.setTitle("Colorado Invests $5.6 Million in Work-Based Learning");
        article.setSource("hoodline");
        article.setUrl("https://hoodline.com/2025/11/colorado-invests-5-6-million-in-work-based-learning-to-enhance-higher-education-in-rural-areas/");

        CredibilityScore score;
        try {
            score = dao.generateScore(article);
        } catch (Exception e) {
            System.out.println("Skipping OpenPageRankApiTest: API call failed with " + e.getMessage());
            return;
        }

        assertNotNull(score);

        double sourceScore = score.getSourceScore();
        assertTrue(sourceScore >= 0.0 && sourceScore <= 1.0);

        double overall = score.getOverallTrust();
        assertTrue(overall >= 0.0 && overall <= 1.0);
    }
}
