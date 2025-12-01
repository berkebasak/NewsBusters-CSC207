package use_case.filter_news;

import java.util.List;

import entity.Article;

/**
 * Data Access Interface for the Filter News use case.
 */
public interface FilterNewsUserDataAccessInterface {

    /**
     * Filters articles that match the selected topics.
     * @param topics the list of topics the user selected
     * @return a list of matching articles
     */
    List<Article> filterByTopics(List<String> topics);
}
