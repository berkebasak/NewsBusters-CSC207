package use_case.filter_news;

import entity.Article;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterNewsInteractorTest {

    @Test
    void successTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("1", "Business News", "Desc", "url1", "img1", "src1"));
        articles.add(new Article("2", "Tech Update", "Desc", "url2", "img2", "src2"));
        articles.add(new Article("3", "Sports News", "Desc", "url3", "img3", "src3"));

        List<String> topics = List.of("business", "technology");
        FilterNewsInputData inputData = new FilterNewsInputData(topics);

        FilterNewsUserDataAccessInterface userRepository = new FilterNewsUserDataAccessInterface() {
            @Override
            public List<Article> filterByTopics(List<String> topics) {
                return articles;
            }
        };

        FilterNewsOutputBoundary successPresenter = new FilterNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(FilterNewsOutputData outputData) {
                assertEquals(3, outputData.getArticles().size());
                assertEquals(topics, outputData.getTopics());
                assertTrue(outputData.getArticles().containsAll(articles));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        FilterNewsInputBoundary interactor =
                new FilterNewsInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void successEmptyTopicsListTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("1", "Article 1", "Desc", "url1", "img1", "src1"));
        articles.add(new Article("2", "Article 2", "Desc", "url2", "img2", "src2"));

        List<String> emptyTopics = Collections.emptyList();
        FilterNewsInputData inputData = new FilterNewsInputData(emptyTopics);

        FilterNewsUserDataAccessInterface userRepository = new FilterNewsUserDataAccessInterface() {
            @Override
            public List<Article> filterByTopics(List<String> topics) {
                return articles;
            }
        };

        FilterNewsOutputBoundary successPresenter = new FilterNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(FilterNewsOutputData outputData) {
                assertEquals(2, outputData.getArticles().size());
                assertEquals(emptyTopics, outputData.getTopics());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        FilterNewsInputBoundary interactor =
                new FilterNewsInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureNullInputDataTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("1", "Article", "Desc", "url", "img", "src"));

        FilterNewsUserDataAccessInterface userRepository = new FilterNewsUserDataAccessInterface() {
            @Override
            public List<Article> filterByTopics(List<String> topics) {
                return articles;
            }
        };

        FilterNewsOutputBoundary failurePresenter = new FilterNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(FilterNewsOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No filter settings provided.", error);
            }
        };

        FilterNewsInputBoundary interactor =
                new FilterNewsInteractor(userRepository, failurePresenter);
        interactor.execute(null);
    }

    @Test
    void failureEmptyArticlesListTest() {
        List<String> topics = List.of("business", "technology");
        FilterNewsInputData inputData = new FilterNewsInputData(topics);

        FilterNewsUserDataAccessInterface userRepository = new FilterNewsUserDataAccessInterface() {
            @Override
            public List<Article> filterByTopics(List<String> topics) {
                return new ArrayList<>();
            }
        };

        FilterNewsOutputBoundary failurePresenter = new FilterNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(FilterNewsOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No articles found for these topics.", error);
            }
        };

        FilterNewsInputBoundary interactor =
                new FilterNewsInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureNullArticlesListTest() {
        List<String> topics = List.of("business");
        FilterNewsInputData inputData = new FilterNewsInputData(topics);

        FilterNewsUserDataAccessInterface userRepository = new FilterNewsUserDataAccessInterface() {
            @Override
            public List<Article> filterByTopics(List<String> topics) {
                return null;
            }
        };

        FilterNewsOutputBoundary failurePresenter = new FilterNewsOutputBoundary() {
            @Override
            public void prepareSuccessView(FilterNewsOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No articles found for these topics.", error);
            }
        };

        FilterNewsInputBoundary interactor =
                new FilterNewsInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}
