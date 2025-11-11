package use_case.top_headlines;

import entity.Article;
import java.util.List;

public interface TopHeadlinesUserDataAccessInterface {
    List<Article> fetchTopHeadlines();
}
