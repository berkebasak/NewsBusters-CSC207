package use_case.search_news;

import java.util.ArrayList;
import java.util.List;
import entity.Article;
import entity.UserPreferences;

/**
 * The interactor for the Search News by Keyword use case.
 * Handles the logic for searching news articles
 */
public class SearchNewsInteractor implements SearchNewsInputBoundary {

    private final SearchNewsUserDataAccessInterface userDataAccessObject;
    private final SearchNewsOutputBoundary searchNewsPresenter;

    /**
     * Constructs a SearchNewsInteractor with the given Data Access Object and Presenter.
     *
     * @param userDataAccessInterface  the data access interface for searching news articles
     * @param searchNewsOutputBoundary the output boundary for presenting search results
     */
    public SearchNewsInteractor(SearchNewsUserDataAccessInterface userDataAccessInterface,
                                SearchNewsOutputBoundary searchNewsOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.searchNewsPresenter = searchNewsOutputBoundary;
    }

    /**
     * Executes the Search News use case with the provided input data.
     *
     * @param searchNewsInputData the input data with the keyword to search for
     */
    @Override
    public void execute(SearchNewsInputData searchNewsInputData) {
        String keyword;

        // Check for null input and trim the keyword
        if (searchNewsInputData.getKeyword() == null) {
            keyword = "";
        } else {
            keyword = searchNewsInputData.getKeyword().trim();
        }

        // Validate keyword input
        if (keyword.isEmpty()) {
            searchNewsPresenter.prepareFailView("Please enter a keyword.");
            return;
        }

        UserPreferences userPreferences = searchNewsInputData.getUserPreferences();

        try {
            // Retrieve articles from the data access object
            List<Article> articles = userDataAccessObject.searchByKeyword(keyword, userPreferences);

            if (articles == null || articles.isEmpty()) {
                searchNewsPresenter.prepareFailView("No articles found.");
                return;
            }

            String lowerKeyword = keyword.toLowerCase();
            List<Article> filtered = new ArrayList<>();

            for (Article a : articles) {
                String title = a.getTitle();
                if (title != null && title.toLowerCase().contains(lowerKeyword)) {
                    filtered.add(a);
                }
            }

            if (filtered.size() > 20) {
                filtered = new ArrayList<>(filtered.subList(0, 20));
            }


            if (filtered.isEmpty()) {
                searchNewsPresenter.prepareFailView("No articles found.");
            } else {
                SearchNewsOutputData searchNewsOutputData =
                        new SearchNewsOutputData(keyword, filtered);
                searchNewsPresenter.prepareSuccessView(searchNewsOutputData);
            }

        } catch (Exception e) {
            searchNewsPresenter.prepareFailView("Search failed. " + e.getMessage());
        }
    }
}