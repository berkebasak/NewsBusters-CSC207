package use_case.top_headlines;

import entity.Article;
import java.util.List;

public class TopHeadlinesOutputData {
    private final List<Article> articles;

    public TopHeadlinesOutputData(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
