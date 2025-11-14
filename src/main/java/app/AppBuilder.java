package app;

import data_access.DBUserDataAccessObject;
import interface_adapter.top_headlines.*;
import use_case.top_headlines.*;
import view.TopHeadlinesView;

public class AppBuilder {

    public static void buildApp() {
        TopHeadlinesUserDataAccessInterface dao = new DBUserDataAccessObject();

        TopHeadlinesState state = new TopHeadlinesState();
        TopHeadlinesViewModel viewModel = new TopHeadlinesViewModel(state);
        TopHeadlinesPresenter presenter = new TopHeadlinesPresenter(viewModel);
        TopHeadlinesInteractor interactor = new TopHeadlinesInteractor(dao, presenter);
        TopHeadlinesController controller = new TopHeadlinesController(interactor);

        javax.swing.SwingUtilities.invokeLater(() -> {
            TopHeadlinesView view = new TopHeadlinesView(controller, viewModel);
            view.setVisible(true);
        });
    }
}
