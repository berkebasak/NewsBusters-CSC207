package use_case.discover_page;

import entity.Article;
import entity.UserPreferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DiscoverPageInteractor.
 */
class DiscoverPageInteractorTest {

    /**
     * Test DAO
     */
    private static class TestDiscoverPageDAO implements DiscoverPageDataAccessInterface {

        private final List<Article> readingHistory;
        private final Set<String> topics;
        private final List<Article> discoveredArticles;
        private final List<Article> page0Articles; // For fallback testing

        TestDiscoverPageDAO(List<Article> readingHistory, Set<String> topics, 
                           List<Article> discoveredArticles, List<Article> page0Articles) {
            this.readingHistory = readingHistory;
            this.topics = topics;
            this.discoveredArticles = discoveredArticles;
            this.page0Articles = page0Articles;
        }

        @Override
        public List<Article> getReadingHistory(String username) {
            return readingHistory;
        }

        @Override
        public Set<String> extractTopTopics(List<Article> articles, int topN) {
            return topics;
        }

        @Override
        public List<Article> searchByTopics(Set<String> topics, int page, UserPreferences up) {
            if (page == 0 && page0Articles != null) {
                return page0Articles;
            }
            return discoveredArticles;
        }
    }

    @Test
    void success_topicsChanged_resetToPage0() {
        List<Article> readingHistory = new ArrayList<>();
        readingHistory.add(new Article("1", "Football game today", "d", "l1", "i1", "s1"));
        readingHistory.add(new Article("2", "Basketball match results", "d", "l2", "i2", "s2"));

        Set<String> topics = new HashSet<>();
        topics.add("football");
        topics.add("basketball");
        topics.add("game");

        List<Article> discoveredArticles = new ArrayList<>();
        discoveredArticles.add(new Article("3", "New football news", "d", "l3", "i3", "s3"));
        discoveredArticles.add(new Article("4", "Basketball update", "d", "l4", "i4", "s4"));

        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(readingHistory, topics, discoveredArticles, null);
        
        // Current topics are different (or null)
        DiscoverPageInputData input = new DiscoverPageInputData(null, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                assertEquals(2, output.getArticles().size());
                assertEquals(topics, output.getTopics());
                assertEquals(0, output.getPage()); // Should reset to page 0
            }

            @Override
            public void prepareNoHistoryView(String message) {
                fail("Should not show no history when reading history exists.");
            }

            @Override
            public void prepareNoArticlesView(String message) {
                fail("Should not show no articles when articles are found.");
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void success_topicsSame_incrementPage() {
        List<Article> readingHistory = new ArrayList<>();
        readingHistory.add(new Article("1", "Football game today", "d", "l1", "i1", "s1"));

        Set<String> topics = new HashSet<>();
        topics.add("football");
        topics.add("game");

        List<Article> discoveredArticles = new ArrayList<>();
        discoveredArticles.add(new Article("3", "New football news", "d", "l3", "i3", "s3"));

        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(readingHistory, topics, discoveredArticles, null);
        
        // Current topics are the same
        DiscoverPageInputData input = new DiscoverPageInputData(topics, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                assertEquals(1, output.getArticles().size());
                assertEquals(topics, output.getTopics());
                assertEquals(1, output.getPage()); // Should increment to page 1
            }

            @Override
            public void prepareNoHistoryView(String message) {
                fail("Should not show no history when reading history exists.");
            }

            @Override
            public void prepareNoArticlesView(String message) {
                fail("Should not show no articles when articles are found.");
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void failure_noReadingHistory() {
        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(new ArrayList<>(), new HashSet<>(), new ArrayList<>(), null);
        DiscoverPageInputData input = new DiscoverPageInputData(null, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                fail("Should not succeed when reading history is empty.");
            }

            @Override
            public void prepareNoHistoryView(String message) {
                assertEquals("Save articles to personalize your Discover feed.", message);
            }

            @Override
            public void prepareNoArticlesView(String message) {
                fail("Should show no history, not no articles.");
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void failure_noTopicsExtracted() {
        List<Article> readingHistory = new ArrayList<>();
        readingHistory.add(new Article("1", "The a an", "d", "l1", "i1", "s1")); // Only stop words

        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(readingHistory, new HashSet<>(), new ArrayList<>(), null);
        DiscoverPageInputData input = new DiscoverPageInputData(null, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                fail("Should not succeed when no topics are extracted.");
            }

            @Override
            public void prepareNoHistoryView(String message) {
                assertEquals("Save articles to personalize your Discover feed.", message);
            }

            @Override
            public void prepareNoArticlesView(String message) {
                fail("Should show no history, not no articles.");
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void success_noArticlesOnPage_fallbackToPage0() {
        List<Article> readingHistory = new ArrayList<>();
        readingHistory.add(new Article("1", "Football game today", "d", "l1", "i1", "s1"));

        Set<String> topics = new HashSet<>();
        topics.add("football");

        List<Article> page0Articles = new ArrayList<>();
        page0Articles.add(new Article("3", "Football news", "d", "l3", "i3", "s3"));

        // No articles on page 1, but articles on page 0
        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(readingHistory, topics, new ArrayList<>(), page0Articles);
        DiscoverPageInputData input = new DiscoverPageInputData(topics, 0, "testUser",
                new UserPreferences()); // Will try page 1, then fallback to 0

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                assertEquals(1, output.getArticles().size());
                assertEquals(0, output.getPage()); // Should fallback to page 0
            }

            @Override
            public void prepareNoHistoryView(String message) {
                fail("Should not show no history when reading history exists.");
            }

            @Override
            public void prepareNoArticlesView(String message) {
                fail("Should not show no articles when page 0 has articles.");
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void failure_noArticlesFound() {
        List<Article> readingHistory = new ArrayList<>();
        readingHistory.add(new Article("1", "Football game today", "d", "l1", "i1", "s1"));

        Set<String> topics = new HashSet<>();
        topics.add("football");

        // No articles on any page
        TestDiscoverPageDAO dao = new TestDiscoverPageDAO(readingHistory, topics, new ArrayList<>(), new ArrayList<>());
        DiscoverPageInputData input = new DiscoverPageInputData(null, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                fail("Should not succeed when no articles are found.");
            }

            @Override
            public void prepareNoHistoryView(String message) {
                fail("Should show no articles, not no history.");
            }

            @Override
            public void prepareNoArticlesView(String message) {
                assertEquals("No new articles available.", message);
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void failure_exceptionHandling() {
        // DAO that throws exception
        DiscoverPageDataAccessInterface dao = new DiscoverPageDataAccessInterface() {
            @Override
            public List<Article> getReadingHistory(String username) {
                throw new RuntimeException("Database error");
            }

            @Override
            public Set<String> extractTopTopics(List<Article> articles, int topN) {
                return new HashSet<>();
            }

            @Override
            public List<Article> searchByTopics(Set<String> topics, int page, UserPreferences userPreferences) {
                return new ArrayList<>();
            }
        };

        DiscoverPageInputData input = new DiscoverPageInputData(null, 0, "testUser",
                new UserPreferences());

        DiscoverPageOutputBoundary presenter = new DiscoverPageOutputBoundary() {
            @Override
            public void prepareSuccessView(DiscoverPageOutputData output) {
                fail("Should not succeed when exception occurs.");
            }

            @Override
            public void prepareNoHistoryView(String message) {
                fail("Should show no articles on exception, not no history.");
            }

            @Override
            public void prepareNoArticlesView(String message) {
                assertEquals("No new articles available.", message);
            }
        };

        DiscoverPageInputBoundary interactor = new DiscoverPageInteractor(dao, presenter);
        interactor.execute(input);
    }
}
