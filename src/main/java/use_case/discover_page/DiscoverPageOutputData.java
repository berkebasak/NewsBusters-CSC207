package use_case.discover_page;

import java.util.List;
import java.util.Set;

import entity.Article;

public class DiscoverPageOutputData {
    private final List<Article> articles;
    private final Set<String> topics;

    public DiscoverPageOutputData(List<Article> articles, Set<String> topics) {
        this.articles = articles;
        this.topics = topics;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Set<String> getTopics() {
        return topics;
    }
}
