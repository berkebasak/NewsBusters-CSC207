package use_case.sort_headlines;

import entity.Article;
import entity.CredibilityScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class SortHeadlinesInteractorTest {

    @Mock
    private SortHeadlinesOutputBoundary mockPresenter;

    private SortHeadlinesInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new SortHeadlinesInteractor(mockPresenter);
    }

    @Test
    void testExecute_WithNullArticles_ShouldCallPrepareFailView() {
        // Arrange
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", null);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareFailView("No articles to sort.");
    }

    @Test
    void testExecute_WithEmptyArticles_ShouldCallPrepareFailView() {
        // Arrange
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", Collections.emptyList());

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareFailView("No articles to sort.");
    }

    @Test
    void testExecute_WithMissingScores_ShouldCallPrepareFailView() {
        // Arrange
        Article article1 = mock(Article.class);
        when(article1.getCredibilityScore()).thenReturn(null);

        List<Article> articles = Collections.singletonList(article1);
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", articles);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareFailView("Sorting unavailable - missing trust scores.");
    }

    @Test
    void testExecute_SortHighToLow_ShouldSortCorrectly() {
        // Arrange
        Article lowScoreArticle = createArticleWithScore(0.5);
        Article highScoreArticle = createArticleWithScore(0.9);
        Article mediumScoreArticle = createArticleWithScore(0.7);

        List<Article> articles = Arrays.asList(lowScoreArticle, highScoreArticle, mediumScoreArticle);
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", articles);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareSuccessView(argThat(output -> {
            List<Article> sorted = output.getArticles();
            return sorted.get(0) == highScoreArticle &&
                    sorted.get(1) == mediumScoreArticle &&
                    sorted.get(2) == lowScoreArticle;
        }));
    }

    @Test
    void testExecute_SortLowToHigh_ShouldSortCorrectly() {
        // Arrange
        Article lowScoreArticle = createArticleWithScore(0.3);
        Article highScoreArticle = createArticleWithScore(0.9);
        Article mediumScoreArticle = createArticleWithScore(0.6);

        List<Article> articles = Arrays.asList(highScoreArticle, mediumScoreArticle, lowScoreArticle);
        SortHeadlinesInputData input = new SortHeadlinesInputData("low", articles);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareSuccessView(argThat(output -> {
            List<Article> sorted = output.getArticles();
            return sorted.get(0) == lowScoreArticle &&
                    sorted.get(1) == mediumScoreArticle &&
                    sorted.get(2) == highScoreArticle;
        }));
    }

    @Test
    void testExecute_WithSingleArticle_ShouldReturnSingleArticle() {
        // Arrange
        Article article = createArticleWithScore(0.8);
        List<Article> articles = Collections.singletonList(article);
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", articles);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getArticles().size() == 1 &&
                        output.getArticles().get(0) == article
        ));
    }

    @Test
    void testExecute_WithEqualScores_ShouldMaintainOrder() {
        // Arrange
        Article article1 = createArticleWithScore(0.5);
        Article article2 = createArticleWithScore(0.5);

        List<Article> articles = Arrays.asList(article1, article2);
        SortHeadlinesInputData input = new SortHeadlinesInputData("high", articles);

        // Act
        interactor.execute(input);

        // Assert
        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getArticles().get(0) == article1 &&
                        output.getArticles().get(1) == article2
        ));
    }

    private Article createArticleWithScore(double score) {
        Article article = mock(Article.class);
        CredibilityScore credibilityScore = mock(CredibilityScore.class);
        when(credibilityScore.getOverallTrust()).thenReturn(score);
        when(article.getCredibilityScore()).thenReturn(credibilityScore);
        return article;
    }
}