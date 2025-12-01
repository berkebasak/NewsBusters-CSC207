package app;

import interface_adapter.ViewManagerModel;
import use_case.filter_news.FilterNewsOutputBoundary;
import view.ViewManager;
import data_access.DBUserDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.generate_credibility.GenerateCredibilityAPIsDataAccessObject;
import interface_adapter.login.*;
import interface_adapter.signup.*;
import interface_adapter.search_news.SearchNewsController;
import interface_adapter.search_news.SearchNewsPresenter;
import interface_adapter.top_headlines.*;
import interface_adapter.save_article.*;
import interface_adapter.discover_page.*;
import interface_adapter.generate_credibility.GenerateCredibilityController;
import interface_adapter.generate_credibility.GenerateCredibilityPresenter;
import interface_adapter.generate_credibility.DiscoverGenerateCredibilityPresenter;
import interface_adapter.filter_credibility.FilterCredibilityController;
import interface_adapter.filter_credibility.TopHeadlinesFilterCredibilityPresenter;
import interface_adapter.filter_credibility.DiscoverPageFilterCredibilityPresenter;
import interface_adapter.view_credibility.ViewCredibilityDetailsViewModel;
import interface_adapter.view_credibility.ViewCredibilityDetailsPresenter;
import interface_adapter.view_credibility.ViewCredibilityDetailsController;
import interface_adapter.profile.*;
import interface_adapter.load_saved_articles.*;
import interface_adapter.profile.ProfileViewModel;
import data_access.save_article.FileSaveArticleDataAccess;
import use_case.login.*;
import use_case.signup.*;
import use_case.top_headlines.*;
import use_case.search_news.SearchNewsInputBoundary;
import use_case.search_news.SearchNewsInteractor;
import use_case.search_news.SearchNewsOutputBoundary;

import interface_adapter.filter_news.FilterNewsController;
import interface_adapter.filter_news.FilterNewsPresenter;
import use_case.filter_news.FilterNewsInputBoundary;
import use_case.filter_news.FilterNewsInteractor;
import use_case.filter_news.FilterNewsUserDataAccessInterface;

import java.io.IOException;

import data_access.save_article.FileSaveArticleDataAccess;
import interface_adapter.save_article.*;

import use_case.save_article.*;
import use_case.discover_page.*;
import use_case.profile.*;
import use_case.generate_credibility.GenerateCredibilityDataAccessInterface;
import use_case.generate_credibility.GenerateCredibilityInputBoundary;
import use_case.generate_credibility.GenerateCredibilityInteractor;
import use_case.generate_credibility.GenerateCredibilityOutputBoundary;
import use_case.view_credibility.ViewCredibilityDetailsInputBoundary;
import use_case.view_credibility.ViewCredibilityDetailsInteractor;
import use_case.view_credibility.ViewCredibilityDetailsOutputBoundary;
import use_case.filter_credibility.FilterCredibilityInputBoundary;
import use_case.filter_credibility.FilterCredibilityInteractor;
import use_case.filter_credibility.FilterCredibilityOutputBoundary;
import use_case.load_saved_articles.*;
import use_case.save_article.SaveArticleDataAccessInterface;
import view.LoginView;
import view.SignupView;
import view.TopHeadlinesView;
import view.DiscoverPageView;
import view.ProfileView;
import view.LoadSavedArticlesView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * The AppBuilder class is responsible for building the NewsBusters application.
 * It provides methods to add different views and use cases to the application.
 */
public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager =
            new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private DBUserDataAccessObject newsDataAccessObject;
    private FileUserDataAccessObject userDataAccessObject;

    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private TopHeadlinesView topHeadlinesView;
    private TopHeadlinesViewModel topHeadlinesViewModel;
    private SaveArticleViewModel saveArticleViewModel;
    private DiscoverPageView discoverPageView;
    private DiscoverPageViewModel discoverPageViewModel;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private ProfileView profileView;
    private ProfileViewModel profileViewModel;
    private LoadSavedArticlesView loadSavedArticlesView;
    private LoadSavedArticlesViewModel loadSavedArticlesViewModel;
    private GenerateCredibilityController generateCredibilityController;
    private ViewCredibilityDetailsViewModel viewCredibilityDetailsViewModel;
    private ViewCredibilityDetailsController credibilityDetailsController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        loginView.setViewManagerModel(viewManagerModel);
        cardPanel.add(loginView, LoginView.VIEW_NAME);
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        signupView.setViewManagerModel(viewManagerModel);
        cardPanel.add(signupView, SignupView.VIEW_NAME);
        return this;
    }

    public AppBuilder addTopHeadlinesView() {
        topHeadlinesViewModel = new TopHeadlinesViewModel();
        topHeadlinesView = new TopHeadlinesView(null, topHeadlinesViewModel);
        topHeadlinesView.setViewManagerModel(viewManagerModel, loginViewModel);
        cardPanel.add(topHeadlinesView, TopHeadlinesView.VIEW_NAME);
        return this;
    }

    public AppBuilder addTopHeadlinesUseCase() {
        if (newsDataAccessObject == null) {
            newsDataAccessObject = new DBUserDataAccessObject();
        }
        TopHeadlinesUserDataAccessInterface dao = newsDataAccessObject;
        TopHeadlinesPresenter presenter = new TopHeadlinesPresenter(topHeadlinesViewModel);
        TopHeadlinesInputBoundary interactor = new TopHeadlinesInteractor(dao, presenter);
        TopHeadlinesController controller = new TopHeadlinesController(interactor);
        topHeadlinesView.setController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary presenter = new LoginPresenter(loginViewModel, viewManagerModel);
        LoginInputBoundary interactor = new LoginInteractor(getUserDataAccessObject(), presenter);
        LoginController controller = new LoginController(interactor);
        loginView.setLoginController(controller);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary presenter =
                new SignupPresenter(signupViewModel, viewManagerModel, loginViewModel);
        SignupInputBoundary interactor = new SignupInteractor(getUserDataAccessObject(), presenter);
        SignupController controller = new SignupController(interactor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addSearchNewsUseCase() {
        if (newsDataAccessObject == null) {
            newsDataAccessObject = new DBUserDataAccessObject();
        }
        SearchNewsOutputBoundary presenter = new SearchNewsPresenter(topHeadlinesViewModel);
        SearchNewsInputBoundary interactor =
                new SearchNewsInteractor(newsDataAccessObject, presenter);
        SearchNewsController controller = new SearchNewsController(interactor);
        topHeadlinesView.setSearchNewsController(controller);
        return this;
    }

    public AppBuilder addFilterNewsUseCase() {
        FilterNewsOutputBoundary presenter = new FilterNewsPresenter(topHeadlinesViewModel);
        FilterNewsInputBoundary interactor = new FilterNewsInteractor(newsDataAccessObject, presenter);
        FilterNewsController controller = new FilterNewsController(interactor);
        topHeadlinesView.setFilterNewsController(controller);
        return this;
    }

    public AppBuilder addSaveArticleUseCase() throws IOException {
        saveArticleViewModel = new SaveArticleViewModel();

        SaveArticleDataAccessInterface saveDao =
                new FileSaveArticleDataAccess("data/saved_articles.txt");
        SaveArticleOutputBoundary savePresenter =
                new SaveArticlePresenter(saveArticleViewModel);
        SaveArticleInputBoundary saveInteractor =
                new SaveArticleInteractor(saveDao, savePresenter,
                        getUserDataAccessObject(), loginViewModel);
        SaveArticleController saveController =
                new SaveArticleController(saveInteractor);
        topHeadlinesView.setSaveArticleUseCase(saveController, saveArticleViewModel);
        return this;
    }

    public AppBuilder addDiscoverPageView() {
        discoverPageViewModel = new DiscoverPageViewModel();
        discoverPageView = new DiscoverPageView(null, discoverPageViewModel);
        discoverPageView.setViewManagerModel(viewManagerModel);
        cardPanel.add(discoverPageView, DiscoverPageView.VIEW_NAME);
        return this;
    }

    public AppBuilder addDiscoverPageUseCase() {
        // Ensure newsDataAccessObject has FileUserDataAccessObject for Discover Page
        if (newsDataAccessObject == null) {
            newsDataAccessObject = new DBUserDataAccessObject(getUserDataAccessObject());
        } else {
            // If it was created without userDataAccess, recreate it with userDataAccess
            // This is safe because DBUserDataAccessObject is stateless except for userDataAccess
            newsDataAccessObject = new DBUserDataAccessObject(getUserDataAccessObject());
        }
        DiscoverPageOutputBoundary presenter = new DiscoverPagePresenter(discoverPageViewModel);
        DiscoverPageInputBoundary interactor =
                new DiscoverPageInteractor(newsDataAccessObject, presenter);
        DiscoverPageController controller =
                new DiscoverPageController(interactor, discoverPageViewModel, loginViewModel);
        discoverPageView.setController(controller);
        return this;
    }

    public AppBuilder addProfileView() {
        profileViewModel = new ProfileViewModel();
        profileView = new ProfileView(profileViewModel);
        profileView.setViewManagerModel(viewManagerModel);
        profileView.setLoginViewModel(loginViewModel);
        cardPanel.add(profileView, ProfileView.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoadSavedArticlesView() {
        loadSavedArticlesViewModel = new LoadSavedArticlesViewModel();
        loadSavedArticlesView = new LoadSavedArticlesView(loadSavedArticlesViewModel);
        loadSavedArticlesView.setViewManagerModel(viewManagerModel);
        loadSavedArticlesView.setLoginViewModel(loginViewModel);
        cardPanel.add(loadSavedArticlesView, LoadSavedArticlesView.VIEW_NAME);
        return this;
    }

    public AppBuilder addProfileUseCase() {
        ProfileOutputBoundary presenter = new ProfilePresenter(profileViewModel, viewManagerModel);
        ProfileInputBoundary interactor = new ProfileInteractor(getUserDataAccessObject(), presenter);
        ProfileController controller = new ProfileController(interactor);

        profileView.setProfileController(controller);

        if (topHeadlinesView != null) {
            topHeadlinesView.setProfileController(controller);
        }
        return this;
    }

    public AppBuilder addLoadSavedArticlesUseCase() throws IOException {
        // use the existing user json DAO
        var userDao = getUserDataAccessObject();

        // optional: use txt data access for coherence check.
        SaveArticleDataAccessInterface savedTxtDao =
                new data_access.save_article.FileSaveArticleDataAccess("data/saved_articles.txt");

        LoadSavedArticlesOutputBoundary presenter =
                new LoadSavedArticlesPresenter(loadSavedArticlesViewModel, viewManagerModel);

        LoadSavedArticlesInputBoundary interactor =
                new LoadSavedArticlesInteractor(userDao, presenter);

        LoadSavedArticlesController controller =
                new LoadSavedArticlesController(interactor);

        // give controller to the ProfileView so the sidebar button can use it
        if (profileView != null) {
            profileView.setLoadSavedArticlesController(controller);
        }

        return this;
    }

    public AppBuilder addCredibilityUseCases() {
        GenerateCredibilityDataAccessInterface signalsDAO =
                new GenerateCredibilityAPIsDataAccessObject();

        GenerateCredibilityOutputBoundary genPresenterTop =
                new GenerateCredibilityPresenter(topHeadlinesViewModel);
        GenerateCredibilityInputBoundary genInteractorTop =
                new GenerateCredibilityInteractor(signalsDAO, genPresenterTop);
        GenerateCredibilityController genControllerTop =
                new GenerateCredibilityController(genInteractorTop);

        GenerateCredibilityOutputBoundary genPresenterDiscover =
                new DiscoverGenerateCredibilityPresenter(discoverPageViewModel);
        GenerateCredibilityInputBoundary genInteractorDiscover =
                new GenerateCredibilityInteractor(signalsDAO, genPresenterDiscover);
        GenerateCredibilityController genControllerDiscover =
                new GenerateCredibilityController(genInteractorDiscover);

        viewCredibilityDetailsViewModel = new ViewCredibilityDetailsViewModel();
        ViewCredibilityDetailsOutputBoundary detailsPresenter =
                new ViewCredibilityDetailsPresenter(viewCredibilityDetailsViewModel);
        ViewCredibilityDetailsInputBoundary detailsInteractor =
                new ViewCredibilityDetailsInteractor(detailsPresenter);
        credibilityDetailsController =
                new ViewCredibilityDetailsController(detailsInteractor);

        this.generateCredibilityController = genControllerTop;

        topHeadlinesView.setCredibilityUseCases(
                genControllerTop,
                credibilityDetailsController,
                viewCredibilityDetailsViewModel
        );

        discoverPageView.setCredibilityUseCases(
                genControllerDiscover,
                credibilityDetailsController,
                viewCredibilityDetailsViewModel
        );

        return this;
    }

    public AppBuilder addFilterCredibilityUseCase() {
        // Create presenter for Top Headlines view
        FilterCredibilityOutputBoundary filterPresenterTop =
                new TopHeadlinesFilterCredibilityPresenter(topHeadlinesViewModel);
        FilterCredibilityInputBoundary filterInteractorTop =
                new FilterCredibilityInteractor(filterPresenterTop);
        FilterCredibilityController filterControllerTop =
                new FilterCredibilityController(filterInteractorTop);

        // Create presenter for Discover Page view
        FilterCredibilityOutputBoundary filterPresenterDiscover =
                new DiscoverPageFilterCredibilityPresenter(discoverPageViewModel);
        FilterCredibilityInputBoundary filterInteractorDiscover =
                new FilterCredibilityInteractor(filterPresenterDiscover);
        FilterCredibilityController filterControllerDiscover =
                new FilterCredibilityController(filterInteractorDiscover);

        // Pass controllers to views
        if (topHeadlinesView != null) {
            topHeadlinesView.setFilterCredibilityController(filterControllerTop);
        }

        if (discoverPageView != null) {
            discoverPageView.setFilterCredibilityController(filterControllerDiscover);
        }

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("NewsBusters");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(TopHeadlinesView.VIEW_NAME);
        viewManagerModel.firePropertyChange();

        viewManager.setHostFrame(application);
        if (loginView != null) {
            viewManagerModel.changeView(LoginView.VIEW_NAME);
        } else {
            viewManagerModel.changeView(TopHeadlinesView.VIEW_NAME);
        }
        return application;
    }

    private FileUserDataAccessObject getUserDataAccessObject() {
        if (userDataAccessObject == null) {
            try {
                userDataAccessObject = new FileUserDataAccessObject("data/users.json");
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize user storage", e);
            }
        }
        return userDataAccessObject;
    }
}
