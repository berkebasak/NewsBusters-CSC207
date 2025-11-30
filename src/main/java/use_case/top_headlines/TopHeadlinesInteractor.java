package use_case.top_headlines;

import entity.Article;
import entity.User;
import data_access.UserDataAccessInterface;
import interface_adapter.login.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesInteractor implements TopHeadlinesInputBoundary {

    private final TopHeadlinesUserDataAccessInterface apiDao;
    private final UserDataAccessInterface userDao;
    private final LoginViewModel loginViewModel;
    private final TopHeadlinesOutputBoundary presenter;

    public TopHeadlinesInteractor(
            TopHeadlinesUserDataAccessInterface apiDao,
            UserDataAccessInterface userDao,
            LoginViewModel loginViewModel,
            TopHeadlinesOutputBoundary presenter) {

        this.apiDao = apiDao;
        this.userDao = userDao;
        this.loginViewModel = loginViewModel;
        this.presenter = presenter;
    }

    @Override
    public void execute(TopHeadlinesInputData inputData) {

        // MAIN FLOW: Fetch articles from the API
        List<Article> apiArticles = null;
        try {
            apiArticles = apiDao.fetchTopHeadlines();
        } catch (Exception ignored) {}

        if (apiArticles != null && !apiArticles.isEmpty()) {

            if (apiArticles.size() > 20) {
                apiArticles = apiArticles.subList(0, 20);
            }

            presenter.present(new TopHeadlinesOutputData(apiArticles));
            return;
        }

        // ALTERNATIVE FLOW: If there is no internet connection or if there is an API Failure
        // load saved articles
        String username = loginViewModel.getState().getUsername();
        User user = userDao.get(username);

        List<Article> saved = user != null ? user.getSavedArticles() : new ArrayList<>();

        if (saved == null) saved = new ArrayList<>();

        if (saved.size() > 20) {
            saved = saved.subList(0, 20);
        }

        presenter.presentAlternative(
                new TopHeadlinesOutputData(saved),
                saved.isEmpty()
                        ? "No internet connection or API Failure.\n No saved articles to show."
                        : "No internet connection or API Failure.\nShowing saved articles."
        );
    }
}
