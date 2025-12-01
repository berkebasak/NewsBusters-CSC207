package use_case.search_news;

import entity.Article;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SearchNewsInteractor.
 */
class SearchNewsInteractorTest {

    /**
     * Test DAO
     */
    private static class TestSearchNewsDAO implements SearchNewsUserDataAccessInterface {

        private final List<Article> articles;

        TestSearchNewsDAO(List<Article> articles) {
            this.articles = articles;
        }

        @Override
        public List<Article> searchByKeyword(String keyword) {
            return articles;
        }
    }

    @Test
    void success_onlyArticlesWithKeywordInTitleAreReturned() {
        String keyword = "covid";

        List<Article> daoArticles = new ArrayList<>();
        daoArticles.add(new Article("1", "Covid cases rising", "d", "l1", "i1", "s1"));
        daoArticles.add(new Article("2", "Market update today", "d", "l2", "i2", "s2"));
        daoArticles.add(new Article("3", "New COVID vaccine approved", "d", "l3", "i3", "s3"));

        TestSearchNewsDAO dao = new TestSearchNewsDAO(daoArticles);
        SearchNewsInputData input = new SearchNewsInputData(keyword);

        SearchNewsOutputBoundary presenter = new SearchNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchNewsOutputData output) {

                assertEquals("covid", output.getKeyword().toLowerCase());

                List<Article> results = output.getArticles();
                assertEquals(2, results.size());

                for (Article a : results) {
                    assertTrue(a.getTitle().toLowerCase().contains("covid"));
                }
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail for a valid keyword.");
            }
        };

        SearchNewsInputBoundary interactor = new SearchNewsInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void failure_emptyKeyword() {
        SearchNewsInputData input = new SearchNewsInputData("   ");
        TestSearchNewsDAO dao = new TestSearchNewsDAO(new ArrayList<>());

        SearchNewsOutputBoundary presenter = new SearchNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchNewsOutputData output) {
                fail("Should not succeed when keyword is empty.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Please enter a keyword.", error);
            }
        };

        SearchNewsInputBoundary interactor = new SearchNewsInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void success_keywordCaseInsensitiveMatching() {
        String keyword = "COVID";

        List<Article> daoArticles = new ArrayList<>();
        daoArticles.add(new Article("1", "covid cases rising", "d", "l1", "i1", "s1"));
        daoArticles.add(new Article("2", "New CoViD treatment discovered", "d", "l2", "i2", "s2"));
        daoArticles.add(new Article("3", "Market update", "d", "l3", "i3", "s3"));

        TestSearchNewsDAO dao = new TestSearchNewsDAO(daoArticles);
        SearchNewsInputData input = new SearchNewsInputData(keyword);

        SearchNewsOutputBoundary presenter = new SearchNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchNewsOutputData output) {
                List<Article> results = output.getArticles();

                // Should return exactly the 2 titles that contain "covid" in any case
                assertEquals(2, results.size());
                for (Article a : results) {
                    assertTrue(a.getTitle().toLowerCase().contains("covid"));
                }
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail for a valid keyword.");
            }
        };

        SearchNewsInputBoundary interactor = new SearchNewsInteractor(dao, presenter);
        interactor.execute(input);
    }

}
