package interface_adapter.top_headlines;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesState {
    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
