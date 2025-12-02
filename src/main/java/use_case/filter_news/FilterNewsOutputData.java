package use_case.filter_news;

import java.util.List;

import entity.Article;

/**
 * Output data for the Filter News use case.
 * Contains the articles found and the topics used for filtering.
 */
public class FilterNewsOutputData {
    private final List<Article> articles;
    private final List<String> topics;

    /**
     * Creates a new FilterNewsOutputData object.
     * @param articles the filtered articles
     * @param topics   the topics used to filter news
     */
    public FilterNewsOutputData(List<Article> articles, List<String> topics) {
        this.articles = articles;
        this.topics = topics;
    }

    /**
     * Returns a list of filtered articles.
     * @return the list of filtered articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Returns the topics used for filtering.
     * @return the topics used for filtering
     */
    public List<String> getTopics() {
        return topics;
    }
}
