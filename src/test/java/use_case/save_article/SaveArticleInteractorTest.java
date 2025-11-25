package use_case.save_article;

import data_access.FileUserDataAccessObject;
import entity.Article;
import entity.User;
import interface_adapter.login.LoginViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveArticleInteractorTest {

    // ===== In-memory fake DAO =====
    private static class InMemorySaveArticleDao implements SaveArticleDataAccessInterface {
        boolean existsReturn = false;
        boolean existsCalled = false;
        boolean saveCalled = false;
        boolean throwOnSave = false;

        String lastUsername;
        Article lastArticle;

        @Override
        public boolean existsByUserandUrl(String username, String url) {
            existsCalled = true;
            return existsReturn;
        }

        @Override
        public void saveForUser(String username, Article article) {
            if (throwOnSave) {
                throw new RuntimeException("Simulated exception");
            }
            saveCalled = true;
            lastUsername = username;
            lastArticle = article;
        }
    }

    // ===== Fake Presenter collecting results =====
    private static class RecordingPresenter implements SaveArticleOutputBoundary {
        SaveArticleOutputData last;

        @Override
        public void present(SaveArticleOutputData outputData) {
            this.last = outputData;
        }
    }

    // ===== Fake User DAO (no filesystem interaction) =====
    private static class FakeUserDao extends FileUserDataAccessObject {

        public FakeUserDao() throws IOException {
            super("test.json");  // must be first
        }

        private final Map<String, User> users = new HashMap<>();

        public void putUser(User user) {
            users.put(user.getUsername(), user);
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public void update(User user) {
            users.put(user.getUsername(), user);
        }
    }

    // ===== Fake LoginViewModel =====
    private static class FakeLoginVM extends LoginViewModel {
        public FakeLoginVM() { super(); }

        public void setUsername(String username) {
            getState().setUsername(username);
        }

        public String getUsername() {
            return getState().getUsername();
        }
    }

    private InMemorySaveArticleDao dataAccess;
    private RecordingPresenter presenter;
    private FakeUserDao userDao;
    private FakeLoginVM loginVM;
    private SaveArticleInteractor interactor;

    @BeforeEach
    void setUp() throws IOException {
        dataAccess = new InMemorySaveArticleDao();
        presenter = new RecordingPresenter();
        userDao = new FakeUserDao();
        loginVM = new FakeLoginVM();
        interactor = new SaveArticleInteractor(dataAccess, presenter, userDao, loginVM);
    }

    private Article makeArticle(String url) {
        return new Article(
                "id",
                "Test Title",
                "desc",
                url,
                "image",
                "source"
        );
    }

    @Test
    void articleNull_showsErrorMessage() {
        SaveArticleInputData input = new SaveArticleInputData(null);

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("You need to select an article first.", presenter.last.getMessage());
        assertFalse(dataAccess.existsCalled);
        assertFalse(dataAccess.saveCalled);
    }

    @Test
    void noLoggedInUser_showsNoLoggedInUserMessage() {
        Article a = makeArticle("https://a.com");
        SaveArticleInputData input = new SaveArticleInputData(a);

        loginVM.setUsername(null); // no login

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("Could not save. No logged-in user.", presenter.last.getMessage());
    }

    @Test
    void userNotFound_showsUserNotFoundMessage() {
        Article a = makeArticle("https://a.com");
        SaveArticleInputData input = new SaveArticleInputData(a);

        loginVM.setUsername("alice"); // but userDao.get returns null

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("Could not save. User not found.", presenter.last.getMessage());
    }

    @Test
    void alreadySaved_showsAlreadySavedMessage() {
        Article a = makeArticle("https://a.com");
        SaveArticleInputData input = new SaveArticleInputData(a);

        loginVM.setUsername("alice");
        User u = User.create("alice", "pwd");
        userDao.putUser(u);

        dataAccess.existsReturn = true;

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("Already saved.", presenter.last.getMessage());
        assertTrue(dataAccess.existsCalled);
        assertFalse(dataAccess.saveCalled);
    }

    @Test
    void successfulSave_updatesUserAndDao() {
        Article a = makeArticle("https://a.com");
        SaveArticleInputData input = new SaveArticleInputData(a);

        loginVM.setUsername("alice");
        User u = User.create("alice", "pwd");
        userDao.putUser(u);

        dataAccess.existsReturn = false;

        interactor.execute(input);

        assertTrue(presenter.last.isSuccess());
        assertEquals("Saved.", presenter.last.getMessage());
        assertTrue(dataAccess.existsCalled);
        assertTrue(dataAccess.saveCalled);
        assertEquals(a, dataAccess.lastArticle);
    }

    @Test
    void exceptionDuringSave_showsCouldNotSaveMessage() {
        Article a = makeArticle("https://a.com");
        SaveArticleInputData input = new SaveArticleInputData(a);

        loginVM.setUsername("alice");
        userDao.putUser(User.create("alice", "pwd"));

        dataAccess.throwOnSave = true;

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("Could not save.", presenter.last.getMessage());
    }
}
