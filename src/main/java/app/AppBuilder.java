package app;

import data_access.DBUserDataAccessObject;

// Top headlines imports
import interface_adapter.ViewManagerModel;
import interface_adapter.top_headlines.*;
import use_case.top_headlines.*;
import view.TopHeadlinesView;

// Search news imports
import interface_adapter.search_news.SearchNewsController;
import interface_adapter.search_news.SearchNewsPresenter;
import interface_adapter.search_news.SearchNewsViewModel;
import use_case.search_news.SearchNewsInputBoundary;
import use_case.search_news.SearchNewsInteractor;
import use_case.search_news.SearchNewsUserDataAccessInterface;

// Views and ViewManager
import view.TopHeadlinesView;
import view.SearchNewsView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private final DBUserDataAccessObject newsDataAccessObject = new DBUserDataAccessObject();

    // Views + ViewModels
    private TopHeadlinesViewModel topHeadlinesViewModel;
    private TopHeadlinesView topHeadlinesView;

    private SearchNewsViewModel searchNewsViewModel;
    private SearchNewsView searchNewsView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }


    public AppBuilder addTopHeadlinesView() {
        TopHeadlinesState state = new TopHeadlinesState();
        topHeadlinesViewModel = new TopHeadlinesViewModel(state);
        topHeadlinesView = new TopHeadlinesView(topHeadlinesViewModel);
        cardPanel.add(topHeadlinesView, topHeadlinesView.getViewName());
        return this;
    }

    public AppBuilder addTopHeadlinesUseCase() {
        TopHeadlinesUserDataAccessInterface dao = newsDataAccessObject;

        TopHeadlinesPresenter presenter = new TopHeadlinesPresenter(topHeadlinesViewModel);
        TopHeadlinesInputBoundary interactor = new TopHeadlinesInteractor(dao, presenter);
        TopHeadlinesController controller = new TopHeadlinesController(interactor);

        topHeadlinesView.setTopHeadlinesController(controller);
        // optional: controller.fetchHeadlines();  // auto-load on startup
        return this;
    }

    public AppBuilder addSearchNewsView() {
        searchNewsViewModel = new SearchNewsViewModel();
        searchNewsView = new SearchNewsView(searchNewsViewModel);
        cardPanel.add(searchNewsView, searchNewsView.getViewName());
        return this;
    }

    public AppBuilder addSearchNewsUseCase() {
        SearchNewsPresenter presenter = new SearchNewsPresenter(searchNewsViewModel);
        SearchNewsInputBoundary interactor = new SearchNewsInteractor(newsDataAccessObject, presenter);
        SearchNewsController controller = new SearchNewsController(interactor);

        searchNewsView.setSearchNewsController(controller);
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("News App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(topHeadlinesView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
