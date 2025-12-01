package use_case.sort_headlines;

import entity.Article;
import java.util.List;

public class SortHeadlinesOutputData {
    private final List<Article> articles;

    public SortHeadlinesOutputData(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
