package use_case.top_headlines;

import entity.Article;
import entity.User;
import data_access.UserDataAccessInterface;
import entity.UserPreferences;
import interface_adapter.login.LoginViewModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopHeadlinesInteractorTest {

    private static class FakeLoginViewModel extends LoginViewModel {
        public FakeLoginViewModel(String username) {
            this.getState().setUsername(username);
        }
    }

    private static class CapturingPresenter implements TopHeadlinesOutputBoundary {
        boolean successCalled = false;
        boolean alternativeCalled = false;
        TopHeadlinesOutputData lastSuccessData;
        TopHeadlinesOutputData lastAlternativeData;
        String lastAlternativeMessage;

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

    private static class FakeUserDao implements UserDataAccessInterface {

        private final User user;

        public FakeUserDao(User user) {
            this.user = user;
        }

        @Override
        public User get(String username) {
            return user != null && user.getUsername().equals(username) ? user : null;
        }

        @Override
        public boolean existsByName(String name) {
            return user != null && user.getUsername().equals(name);
        }

        @Override
        public void save(User user) {
        }

        @Override
        public void update(User user) {
        }

        @Override
        public List<User> getAll() {
            return user == null ? new ArrayList<>() : List.of(user);
        }

    }

    @Test
    void mainFlowLimitsToTwentyArticles() {

        TopHeadlinesUserDataAccessInterface fakeApi = (up) -> {
            List<Article> list = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                Article a = new Article();
                a.setTitle("Headline " + i);
                a.setUrl("https://example.com/" + i);
                list.add(a);
            }
            return list;
        };

        User user = User.fromPersistence("berke", "1234", new ArrayList<>(),
                new ArrayList<>(), new UserPreferences());
        FakeUserDao fakeUserDao = new FakeUserDao(user);

        FakeLoginViewModel loginVM = new FakeLoginViewModel("berke");
        CapturingPresenter presenter = new CapturingPresenter();

        TopHeadlinesInteractor interactor =
                new TopHeadlinesInteractor(fakeApi, fakeUserDao, loginVM, presenter);

        interactor.execute(new TopHeadlinesInputData("top", user.getUserPreferences()));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.alternativeCalled);

        List<Article> result = presenter.lastSuccessData.getArticles();
        assertEquals(20, result.size());
        assertEquals("Headline 0", result.get(0).getTitle());
        assertEquals("Headline 19", result.get(19).getTitle());
    }

    @Test
    void alternativeFlowUsesSavedArticlesFromUser() {

        TopHeadlinesUserDataAccessInterface fakeApi = (up) -> {return new ArrayList<>();};

        List<Article> saved = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Article a = new Article();
            a.setTitle("Saved " + i);
            a.setUrl("https://example.com/s-" + i);
            saved.add(a);
        }

        User user = User.fromPersistence("berke", "1234", saved,
                new ArrayList<>(), new UserPreferences());
        FakeUserDao fakeUserDao = new FakeUserDao(user);

        FakeLoginViewModel loginVM = new FakeLoginViewModel("berke");
        CapturingPresenter presenter = new CapturingPresenter();

        TopHeadlinesInteractor interactor =
                new TopHeadlinesInteractor(fakeApi, fakeUserDao, loginVM, presenter);

        interactor.execute(new TopHeadlinesInputData("top", user.getUserPreferences()));

        assertFalse(presenter.successCalled);
        assertTrue(presenter.alternativeCalled);

        List<Article> result = presenter.lastAlternativeData.getArticles();
        assertEquals(3, result.size());
        assertEquals("Saved 0", result.get(0).getTitle());
        assertEquals("Saved 2", result.get(2).getTitle());
        assertTrue(presenter.lastAlternativeMessage.contains("saved"));
    }

    @Test
    void alternativeFlowAlsoLimitsToTwentyArticles() {

        TopHeadlinesUserDataAccessInterface fakeApi = (up) -> null;

        List<Article> saved = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Article a = new Article();
            a.setTitle("Saved " + i);
            a.setUrl("https://example.com/s-" + i);
            saved.add(a);
        }

        User user = User.fromPersistence("berke", "1234", saved,
                new ArrayList<>(), new UserPreferences());
        FakeUserDao fakeUserDao = new FakeUserDao(user);

        FakeLoginViewModel loginVM = new FakeLoginViewModel("berke");
        CapturingPresenter presenter = new CapturingPresenter();

        TopHeadlinesInteractor interactor =
                new TopHeadlinesInteractor(fakeApi, fakeUserDao, loginVM, presenter);

        interactor.execute(new TopHeadlinesInputData("top", user.getUserPreferences()));

        assertTrue(presenter.alternativeCalled);

        List<Article> result = presenter.lastAlternativeData.getArticles();
        assertEquals(20, result.size());
        assertEquals("Saved 0", result.get(0).getTitle());
        assertEquals("Saved 19", result.get(19).getTitle());
    }

    @Test
    void alternativeFlowWhenUserDoesNotExist() {

        // API returns empty list → alternative flow
        TopHeadlinesUserDataAccessInterface fakeApi = (up) -> new ArrayList<>();

        FakeUserDao dao = new FakeUserDao(null);  // ← no user returned
        FakeLoginViewModel loginVM = new FakeLoginViewModel("ghostUser");
        CapturingPresenter presenter = new CapturingPresenter();

        TopHeadlinesInteractor interactor =
                new TopHeadlinesInteractor(fakeApi, dao, loginVM, presenter);

        interactor.execute(new TopHeadlinesInputData("top", new UserPreferences()));

        assertTrue(presenter.alternativeCalled);
        assertEquals(0, presenter.lastAlternativeData.getArticles().size());
    }

    @Test
    void mainFlowExactlyTwentyArticlesNoTrimming() {

        TopHeadlinesUserDataAccessInterface fakeApi = (up) -> {
            List<Article> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Article a = new Article();
                a.setTitle("Headline " + i);
                a.setUrl("https://example.com/" + i);
                list.add(a);
            }
            return list; // EXACTLY 20
        };

        User user = User.fromPersistence(
                "berke",
                "1234",
                new ArrayList<>(),
                new ArrayList<>(),
                new UserPreferences()
        );

        FakeUserDao dao = new FakeUserDao(user);
        CapturingPresenter presenter = new CapturingPresenter();
        FakeLoginViewModel loginVM = new FakeLoginViewModel("berke");

        TopHeadlinesInteractor interactor =
                new TopHeadlinesInteractor(fakeApi, dao, loginVM, presenter);

        interactor.execute(new TopHeadlinesInputData("top", user.getUserPreferences()));

        assertTrue(presenter.successCalled);
        assertEquals(20, presenter.lastSuccessData.getArticles().size());
    }


}
