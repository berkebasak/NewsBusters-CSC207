package use_case.filter_credibility;

import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FilterCredibilityInteractor
 */
class FilterCredibilityInteractorTest {

    /**
     * Helper method to create an article with a credibility score.
     */
    private Article createArticleWithScore(String id, String title, String level) {
        // Create credibility score that results in the desired level
        // High: overallTrust >= 0.8
        // Medium: overallTrust >= 0.65
        // Low: overallTrust < 0.65

        double sourceScore, sentimentScore, claimConfidence;
        
        if ("High".equals(level)) {
            sourceScore = 0.8;
            sentimentScore = 0.9;
            claimConfidence = 0.9;
        } else if ("Medium".equals(level)) {
            sourceScore = 0.5;
            sentimentScore = 0.7;
            claimConfidence = 0.7;
        } else {
            sourceScore = 0.3;
            sentimentScore = 0.4;
            claimConfidence = 0.4;
        }
        
        CredibilityScore credibilityScore = new CredibilityScore(sourceScore, sentimentScore, claimConfidence);
        Article article = new Article(id, title, "description", "url", "imageUrl", "source");
        article.setCredibilityScore(credibilityScore);
        article.setConfidenceLevel(credibilityScore.getLevel());
        article.setTrustScore(credibilityScore.getOverallTrust());
        
        return article;
    }

    @Test
    void success_filterByHighTrust() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));
        articles.add(createArticleWithScore("3", "Low Trust Article", "Low"));
        articles.add(createArticleWithScore("4", "Another High Trust Article", "High"));

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(2, output.getFilteredArticles().size());
                assertEquals(filterLevels, output.getFilterLevels());
                // Verify all filtered articles are High trust
                for (Article article : output.getFilteredArticles()) {
                    assertEquals("High", article.getConfidenceLevel());
                }
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filtering succeeds.");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void success_filterByMultipleLevels() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));
        articles.add(createArticleWithScore("3", "Low Trust Article", "Low"));
        articles.add(createArticleWithScore("4", "Another High Trust Article", "High"));
        articles.add(createArticleWithScore("5", "Another Medium Trust Article", "Medium"));

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");
        filterLevels.add("Medium");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(4, output.getFilteredArticles().size());
                assertEquals(filterLevels, output.getFilterLevels());
                // Verify all filtered articles are High or Medium trust
                for (Article article : output.getFilteredArticles()) {
                    String level = article.getConfidenceLevel();
                    assertTrue("High".equals(level) || "Medium".equals(level),
                            "Article should be High or Medium trust");
                }
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filtering succeeds.");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void success_emptyFilterLevels_returnsAllArticles() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));
        articles.add(createArticleWithScore("3", "Low Trust Article", "Low"));

        Set<String> filterLevels = new HashSet<>(); // Empty set

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(3, output.getFilteredArticles().size());
                assertTrue(output.getFilterLevels().isEmpty());

                assertEquals(articles, output.getFilteredArticles());
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filter levels are empty.");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void success_nullFilterLevels_returnsAllArticles() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(2, output.getFilteredArticles().size());
                assertTrue(output.getFilterLevels().isEmpty());
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filter levels are null.");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, null);
        interactor.execute(inputData);
    }

    @Test
    void error_notAllArticlesHaveScores() {
        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "Article with score", "High"));
        

        Article articleWithoutScore = new Article("2", "Article without score", "d", "url", "img", "source");
        articles.add(articleWithoutScore);

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                fail("Should not present success when not all articles have scores.");
            }

            @Override
            public void presentError(String errorMessage) {
                assertEquals("Generate all scores to filter.", errorMessage);
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void error_articleWithUnknownLevel() {
        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "Article with score", "High"));
        

        Article articleWithUnknown = new Article("2", "Article with unknown", "d", "url", "img", "source");
        CredibilityScore score = new CredibilityScore(0.5, 0.5, 0.5);
        articleWithUnknown.setCredibilityScore(score);
        articleWithUnknown.setConfidenceLevel("Unknown");
        articles.add(articleWithUnknown);

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                fail("Should not present success when article has Unknown level.");
            }

            @Override
            public void presentError(String errorMessage) {
                assertEquals("Generate all scores to filter.", errorMessage);
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void error_noArticlesMatchFilter() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("Low");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                fail("Should not present success when no articles match the filter.");
            }

            @Override
            public void presentError(String errorMessage) {
                assertEquals("No articles found at the selected trust levels.", errorMessage);
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void error_emptyArticlesList() {
        List<Article> articles = new ArrayList<>(); // Empty list

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                fail("Should not present success when articles list is empty.");
            }

            @Override
            public void presentError(String errorMessage) {
                assertEquals("Generate all scores to filter.", errorMessage);
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void success_caseInsensitiveFiltering() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));
        articles.add(createArticleWithScore("3", "Low Trust Article", "Low"));

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("high"); // lowercase

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(1, output.getFilteredArticles().size());
                assertEquals("High", output.getFilteredArticles().get(0).getConfidenceLevel());
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filtering succeeds (case-insensitive).");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }

    @Test
    void success_filterByAllLevels() {

        List<Article> articles = new ArrayList<>();
        articles.add(createArticleWithScore("1", "High Trust Article", "High"));
        articles.add(createArticleWithScore("2", "Medium Trust Article", "Medium"));
        articles.add(createArticleWithScore("3", "Low Trust Article", "Low"));

        Set<String> filterLevels = new HashSet<>();
        filterLevels.add("High");
        filterLevels.add("Medium");
        filterLevels.add("Low");

        FilterCredibilityOutputBoundary presenter = new FilterCredibilityOutputBoundary() {
            @Override
            public void presentSuccess(FilterCredibilityOutputData output) {
                assertEquals(3, output.getFilteredArticles().size());
                assertEquals(filterLevels, output.getFilterLevels());
            }

            @Override
            public void presentError(String errorMessage) {
                fail("Should not present error when filtering by all levels.");
            }
        };

        FilterCredibilityInteractor interactor = new FilterCredibilityInteractor(presenter);
        FilterCredibilityInputData inputData = new FilterCredibilityInputData(articles, filterLevels);
        interactor.execute(inputData);
    }
}
