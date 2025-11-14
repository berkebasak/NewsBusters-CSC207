package interface_adapter.search_news;

import java.util.List;
import entity.Article;

/**
 * Holds UI state for Search News.
 */
public class SearchNewsState {
    private String keyword = "";
    private List<Article> articles;
    private String error;

    /**
     *  Makes a copy of another SearchNewsState.
     * @param copy copy the state to copy
     */
    public SearchNewsState(SearchNewsState copy) {
        this.keyword = copy.keyword;
        this.articles = copy.articles;
        this.error = copy.error;
    }

    // creates a new empty state.
    public SearchNewsState() {

    }

    /**
     * Gets the search keyword.
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the search keyword.
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets the list of articles.
     * @return the articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Sets the list of articles.
     * @param articles the list to set
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     * @param error the message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
