package interface_adapter.top_headlines;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesState {
    private List<Article> articles = new ArrayList<>();
    private String error;
    private String articleSourceLabel = "New Articles";

    public List<Article> getArticles() {
        return articles;
    }

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

    public String getArticleSourceLabel() {
        return articleSourceLabel;
    }

    public void setArticleSourceLabel(String articleSourceLabel) {
        this.articleSourceLabel = articleSourceLabel;
    }

}
