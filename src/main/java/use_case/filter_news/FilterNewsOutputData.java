package use_case.filter_news;

import entity.Article;
import java.util.List;

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
     * @return the list of filtered articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * @return the topics used for filtering
     */
    public List<String> getTopics() {
        return topics;
    }
}
