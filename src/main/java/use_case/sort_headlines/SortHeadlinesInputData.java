package use_case.sort_headlines;

import entity.Article;
import java.util.List;

public class SortHeadlinesInputData {
    private final String sortOrder;
    private final List<Article> articles;

    public SortHeadlinesInputData(String sortOrder, List<Article> articles) {
        this.sortOrder = sortOrder;
        this.articles = articles;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
