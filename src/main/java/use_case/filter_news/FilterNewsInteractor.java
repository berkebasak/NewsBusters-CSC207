package use_case.filter_news;

import entity.Article;

import java.util.List;

/**
 * Interactor for the Filter News use case.
 * Uses the Data Access object to fetch articles for the selected topics.
 */
public class FilterNewsInteractor implements FilterNewsInputBoundary {

    private final FilterNewsUserDataAccessInterface userDataAccessObject;
    private final FilterNewsOutputBoundary filterNewsPresenter;

    /**
     * Creates a new FilterNewsInteractor.
     * @param userDataAccessInterface the Data Access object used to fetch filtered articles
     * @param filterNewsOutputBoundary  the Output Boundary that prepares the view
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
        List<String> topics = inputData.getTopics();

        if (topics == null || topics.isEmpty()) {
            filterNewsPresenter.prepareFailView("Please select at least one topic.");
            return;
        }

        List<Article> articles = userDataAccessObject.filterByTopics(topics);

        if (articles == null || articles.isEmpty()) {
            filterNewsPresenter.prepareFailView("No articles found for the selected topics.");
        } else {
            FilterNewsOutputData outputData = new FilterNewsOutputData(articles, topics);
            filterNewsPresenter.prepareSuccessView(outputData);
        }
    }
}
