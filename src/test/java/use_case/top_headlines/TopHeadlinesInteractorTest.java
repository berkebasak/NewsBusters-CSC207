package use_case.top_headlines;

import data_access.DBUserDataAccessObject;
import entity.Article;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopHeadlinesInteractorTest {


    private static class CapturingPresenter implements TopHeadlinesOutputBoundary {
        TopHeadlinesOutputData lastSuccessData;
        TopHeadlinesOutputData lastAlternativeData;
        String lastAlternativeMessage;
        boolean successCalled = false;
        boolean alternativeCalled = false;

        @Override
        public void present(TopHeadlinesOutputData outputData) {
            successCalled = true;
            lastSuccessData = outputData;
        }

        @Override
        public void presentAlternative(TopHeadlinesOutputData outputData, String message) {
            alternativeCalled = true;
            lastAlternativeData = outputData;
            lastAlternativeMessage = message;
        }
    }

    @Test
    void limitsToTwentyArticles() {
        TopHeadlinesUserDataAccessInterface fakeDao = new TopHeadlinesUserDataAccessInterface() {
            @Override
            public List<Article> fetchTopHeadlines() {
                List<Article> list = new ArrayList<>();
                for (int i = 0; i < 25; i++) {
                    Article a = new Article();
                    a.setTitle("Headline " + i);
                    a.setSource("example-source");
                    a.setUrl("https://example.com/article-" + i);
                    list.add(a);
                }
                return list;
            }
        };

        DBUserDataAccessObject fakeDbDao = new DBUserDataAccessObject() {
            @Override
            public List<Article> getReadingHistory() {
                return new ArrayList<>();
            }
        };

        CapturingPresenter presenter = new CapturingPresenter();
        TopHeadlinesInputBoundary interactor =
                new TopHeadlinesInteractor(fakeDao, fakeDbDao, presenter);

        TopHeadlinesInputData inputData = new TopHeadlinesInputData("top");
        interactor.execute(inputData);

        assertTrue(presenter.successCalled, "Main flow should be used.");
        assertFalse(presenter.alternativeCalled, "Fallback flow should not be used.");

        assertNotNull(presenter.lastSuccessData);
        List<Article> result = presenter.lastSuccessData.getArticles();
        assertNotNull(result);
        assertEquals(20, result.size());
        assertEquals("Headline 0", result.get(0).getTitle());
        assertEquals("Headline 19", result.get(19).getTitle());
    }

    @Test
    void usesSavedArticlesWhenApiReturnsEmptyList() {
        TopHeadlinesUserDataAccessInterface fakeDao = new TopHeadlinesUserDataAccessInterface() {
            @Override
            public List<Article> fetchTopHeadlines() {
                return new ArrayList<>();
            }
        };

        DBUserDataAccessObject fakeDbDao = new DBUserDataAccessObject() {
            @Override
            public List<Article> getReadingHistory() {
                List<Article> saved = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    Article a = new Article();
                    a.setTitle("Saved " + i);
                    a.setSource("saved-source");
                    a.setUrl("https://example.com/saved-" + i);
                    saved.add(a);
                }
                return saved;
            }
        };

        CapturingPresenter presenter = new CapturingPresenter();
        TopHeadlinesInputBoundary interactor =
                new TopHeadlinesInteractor(fakeDao, fakeDbDao, presenter);

        TopHeadlinesInputData inputData = new TopHeadlinesInputData("top");
        interactor.execute(inputData);

        assertFalse(presenter.successCalled, "Main flow should not be used when API returns empty.");
        assertTrue(presenter.alternativeCalled, "Fallback flow should be used.");

        assertNotNull(presenter.lastAlternativeData);
        List<Article> result = presenter.lastAlternativeData.getArticles();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Saved 0", result.get(0).getTitle());
        assertEquals("Saved 2", result.get(2).getTitle());
        assertNotNull(presenter.lastAlternativeMessage);
        assertTrue(presenter.lastAlternativeMessage.toLowerCase().contains("showing saved"),
                "Fallback message should mention saved articles.");
    }

    @Test
    void fallbackAlsoLimitsToTwentyArticles() {
        TopHeadlinesUserDataAccessInterface fakeDao = new TopHeadlinesUserDataAccessInterface() {
            @Override
            public List<Article> fetchTopHeadlines() {
                return null;
            }
        };

        DBUserDataAccessObject fakeDbDao = new DBUserDataAccessObject() {
            @Override
            public List<Article> getReadingHistory() {
                List<Article> saved = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    Article a = new Article();
                    a.setTitle("Saved " + i);
                    a.setSource("saved-source");
                    a.setUrl("https://example.com/saved-" + i);
                    saved.add(a);
                }
                return saved;
            }
        };

        CapturingPresenter presenter = new CapturingPresenter();
        TopHeadlinesInputBoundary interactor =
                new TopHeadlinesInteractor(fakeDao, fakeDbDao, presenter);

        TopHeadlinesInputData inputData = new TopHeadlinesInputData("top");
        interactor.execute(inputData);

        assertFalse(presenter.successCalled, "Main flow should not be used when API returns null.");
        assertTrue(presenter.alternativeCalled, "Fallback flow should be used.");

        assertNotNull(presenter.lastAlternativeData);
        List<Article> result = presenter.lastAlternativeData.getArticles();
        assertNotNull(result);
        assertEquals(20, result.size(), "Fallback should also trim to 20 articles.");
        assertEquals("Saved 0", result.get(0).getTitle());
        assertEquals("Saved 19", result.get(19).getTitle());
    }
}
