package use_case.load_saved_articles;

import data_access.UserDataAccessInterface;
import entity.Article;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.save_article.SaveArticleDataAccessInterface;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadSavedArticlesInteractorTest {

    // ---- Simple in-memory User DAO stub ----
    private static class InMemoryUserDao implements UserDataAccessInterface {
        private User storedUser;

        @Override
        public boolean existsByName(String username) {
            return storedUser != null && storedUser.getUsername().equals(username);
        }

        @Override
        public void save(User user) {
            this.storedUser = user;
        }

        @Override
        public void update(User user) {
            this.storedUser = user;
        }

        @Override
        public User get(String username) {
            return existsByName(username) ? storedUser : null;
        }

        @Override
        public Collection<User> getAll() {
            return storedUser == null ? List.of() : List.of(storedUser);
        }
    }

    // ---- Fake txt DAO to control existsByUserandUrl behaviour ----
    private static class StubSavedTxtDao implements SaveArticleDataAccessInterface {
        private final boolean existsResult;

        StubSavedTxtDao(boolean existsResult) {
            this.existsResult = existsResult;
        }

        @Override
        public boolean existsByUserandUrl(String username, String url) {
            return existsResult;
        }

        @Override
        public void saveForUser(String username,Article article) {
            // no-op for tests
        }
    }

    // ---- Presenter stub to capture success / fail calls ----
    private static class TestPresenter implements LoadSavedArticlesOutputBoundary {
        LoadSavedArticlesOutputData successData;
        String failMessage;

        @Override
        public void prepareSuccessView(LoadSavedArticlesOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failMessage = errorMessage;
        }
    }

    // Helper to build a dummy Article (adjust constructor if needed)
    private static Article makeArticle(String url) {
        return new Article(
                "id",
                "title",
                "desc",
                url,
                "imageUrl",
                "source",
                "content",
                null,
                null,
                null,
                0.0,
                "Unknown",
                true
        );
    }

    @Test
    void execute_noUsername_failsWithNoLoggedInUser() {
        InMemoryUserDao userDao = new InMemoryUserDao();
        TestPresenter presenter = new TestPresenter();
        LoadSavedArticlesInteractor interactor =
                new LoadSavedArticlesInteractor(userDao, presenter, null);

        interactor.execute(new LoadSavedArticlesInputData(null));

        assertNull(presenter.successData);
        assertEquals("No logged-in user.", presenter.failMessage);
    }

    @Test
    void execute_userNotFound_failsWithUserNotFound() {
        InMemoryUserDao userDao = new InMemoryUserDao(); // no user saved
        TestPresenter presenter = new TestPresenter();
        LoadSavedArticlesInteractor interactor =
                new LoadSavedArticlesInteractor(userDao, presenter, null);

        interactor.execute(new LoadSavedArticlesInputData("alice"));

        assertNull(presenter.successData);
        assertEquals("User not found.", presenter.failMessage);
    }

    @Test
    void execute_success_noTxtCheck() {
        InMemoryUserDao userDao = new InMemoryUserDao();
        User user = User.create("alice", "pw");
        user.addSavedArticle(makeArticle("https://example.com/a1"));
        userDao.save(user);

        TestPresenter presenter = new TestPresenter();
        LoadSavedArticlesInteractor interactor =
                new LoadSavedArticlesInteractor(userDao, presenter, null);

        interactor.execute(new LoadSavedArticlesInputData("alice"));

        assertNull(presenter.failMessage);
        assertNotNull(presenter.successData);

        assertEquals("alice", presenter.successData.getUsername());
        assertEquals(1, presenter.successData.getSavedArticles().size());
        assertEquals("https://example.com/a1",
                presenter.successData.getSavedArticles().get(0).getUrl());
    }

    @Test
    void execute_success_withTxtCheck_warningPath() {
        InMemoryUserDao userDao = new InMemoryUserDao();
        User user = User.create("bob", "pw");
        user.addSavedArticle(makeArticle("https://example.com/missing"));
        userDao.save(user);

        TestPresenter presenter = new TestPresenter();
        // txt DAO returns false so we hit the println warning branch
        StubSavedTxtDao txtDao = new StubSavedTxtDao(false);

        LoadSavedArticlesInteractor interactor =
                new LoadSavedArticlesInteractor(userDao, presenter, txtDao);

        interactor.execute(new LoadSavedArticlesInputData("bob"));

        assertNull(presenter.failMessage);
        assertNotNull(presenter.successData);
        assertEquals("bob", presenter.successData.getUsername());
        assertEquals(1, presenter.successData.getSavedArticles().size());
    }
}
