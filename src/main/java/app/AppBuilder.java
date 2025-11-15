package app;

import interface_adapter.ViewManagerModel;
import view.ViewManager;

import data_access.DBUserDataAccessObject;
import interface_adapter.top_headlines.*;
import use_case.top_headlines.*;
import view.TopHeadlinesView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import data_access.save_article.FileSaveArticleDataAccess;
import interface_adapter.save_article.*;
import use_case.save_article.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager =
            new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private TopHeadlinesView topHeadlinesView;
    private TopHeadlinesViewModel topHeadlinesViewModel;

    private SaveArticleViewModel saveArticleViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addTopHeadlinesView() {
        topHeadlinesViewModel = new TopHeadlinesViewModel(new TopHeadlinesState());
        topHeadlinesView = new TopHeadlinesView(null, topHeadlinesViewModel);
        cardPanel.add(topHeadlinesView, TopHeadlinesView.VIEW_NAME);
        return this;
    }

    public AppBuilder addTopHeadlinesUseCase() {
        TopHeadlinesUserDataAccessInterface dao = new DBUserDataAccessObject();
        TopHeadlinesPresenter presenter = new TopHeadlinesPresenter(topHeadlinesViewModel);
        TopHeadlinesInputBoundary interactor = new TopHeadlinesInteractor(dao, presenter);
        TopHeadlinesController controller = new TopHeadlinesController(interactor);
        topHeadlinesView.setController(controller);
        return this;
    }

    public AppBuilder addSaveArticleUseCase() throws IOException {
        saveArticleViewModel = new SaveArticleViewModel();
        SaveArticleDataAccessInterface saveDao =
                new FileSaveArticleDataAccess("data/saved_articles.txt");
        SaveArticleOutputBoundary savePresenter =
                new SaveArticlePresenter(saveArticleViewModel);
        SaveArticleInputBoundary saveInteractor =
                new SaveArticleInteractor(saveDao, savePresenter);
        SaveArticleController saveController
                = new SaveArticleController(saveInteractor);
        topHeadlinesView.setSaveArticleUseCase(saveController, saveArticleViewModel);
        return this;
    }


    public JFrame build() {
        JFrame application = new JFrame("NewsBusters");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(900, 600);
        application.add(cardPanel);
        viewManagerModel.setState(TopHeadlinesView.VIEW_NAME);
        viewManagerModel.firePropertyChange();
        return application;
    }
}