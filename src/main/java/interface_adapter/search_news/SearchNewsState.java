package interface_adapter.search_news;

import entity.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the Search News view.
 */
public class SearchNewsState {

    private List<Article> articles = new ArrayList<>();
    private String error;
    private String articleSourceLabel = "Search Results";
    private String keyword = "";

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getArticleSourceLabel() {
        return articleSourceLabel;
    }

    public void setArticleSourceLabel(String articleSourceLabel) {
        this.articleSourceLabel = articleSourceLabel;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
