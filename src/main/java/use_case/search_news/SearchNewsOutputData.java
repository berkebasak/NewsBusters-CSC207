package use_case.search_news;

import java.util.List;
import entity.Article;

/**
 * Output Data for the Login Use Case.
 * Holds the search results and keyword.
 */
public class SearchNewsOutputData {
    private final String keyword;
    private final List<Article> articles;

    /**
     * Creates a new SearchNewsOutputData object, output data with search results.
     * @param keyword  the search keyword used
     * @param articles the list of articles matching the keyword
     */
    public SearchNewsOutputData(String keyword, List<Article> articles) {
        this.keyword = keyword;
        this.articles = articles;
    }

    /**
     * Gets the keyword that was searched.
     * @return the search keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the list of articles that matched the keyword.
     * @return a list of Article entities
     */
    public List<Article> getArticles() {
        return articles;
    }
}
