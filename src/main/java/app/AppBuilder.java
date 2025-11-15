package app;

import data_access.TopHeadlinesDataAccessObject;
import data_access.save_article.FileSaveArticleDataAccess;

import interface_adapter.top_headlines.*;
import interface_adapter.save_article.*;

import use_case.top_headlines.*;
import use_case.save_article.*;

import view.TopHeadlinesView;

import java.io.IOException;

public class AppBuilder {

    public static void buildApp() throws IOException {
        TopHeadlinesUserDataAccessInterface dao = new TopHeadlinesDataAccessObject();

        TopHeadlinesState state = new TopHeadlinesState();
        TopHeadlinesViewModel viewModel = new TopHeadlinesViewModel(state);
        TopHeadlinesPresenter presenter = new TopHeadlinesPresenter(viewModel);
        TopHeadlinesInteractor interactor = new TopHeadlinesInteractor(dao, presenter);
        TopHeadlinesController controller = new TopHeadlinesController(interactor);

        SaveArticleDataAccessInterface saveDao = new FileSaveArticleDataAccess("data/saved_articles.txt");

        SaveArticleViewModel saveViewModel = new SaveArticleViewModel();
        SaveArticlePresenter savePresenter = new SaveArticlePresenter(saveViewModel);
        SaveArticleInteractor saveInteractor = new SaveArticleInteractor(saveDao, savePresenter);
        SaveArticleController saveController = new SaveArticleController(saveInteractor);

        javax.swing.SwingUtilities.invokeLater(() -> {
            TopHeadlinesView view = new TopHeadlinesView(controller, viewModel,
                    saveController, saveViewModel);
            view.setVisible(true);
        });
    }
}
