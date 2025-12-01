package use_case.filter_news;

import java.util.List;

import entity.Article;

/**
 * Interactor for the Filter News use case.
 * Uses the Data Access object to fetch articles for the selected topics.
 */
public class FilterNewsInteractor implements FilterNewsInputBoundary {

    private final FilterNewsUserDataAccessInterface userDataAccessObject;
    private final FilterNewsOutputBoundary filterNewsPresenter;

    /**
     * Creates a new FilterNewsInteractor.
     * @param userDataAccessInterface  the Data Access object used to fetch filtered articles
     * @param filterNewsOutputBoundary the Output Boundary that prepares the view
     */
    public FilterNewsInteractor(FilterNewsUserDataAccessInterface userDataAccessInterface,
                                FilterNewsOutputBoundary filterNewsOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.filterNewsPresenter = filterNewsOutputBoundary;
    }

    /**
     * Runs the Filter News use case.
     * @param inputData the topics selected by the user
     */
    @Override
    public void execute(FilterNewsInputData inputData) {
        if (inputData == null) {
            filterNewsPresenter.prepareFailView("No filter settings provided.");
            return;
        }

        final List<String> topics = inputData.getTopics();

        // DAO will interpret empty topics as "clear filter"
        final List<Article> articles = userDataAccessObject.filterByTopics(topics);

        if (articles == null || articles.isEmpty()) {
            filterNewsPresenter.prepareFailView("No articles found for these topics.");
            return;
        }

        java.util.Collections.shuffle(articles);

        final FilterNewsOutputData outputData = new FilterNewsOutputData(articles, topics);
        filterNewsPresenter.prepareSuccessView(outputData);
    }

}