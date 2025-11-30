package use_case.top_headlines;

import entity.Article;
import entity.UserPreferences;

import java.util.List;

public interface TopHeadlinesUserDataAccessInterface {
    List<Article> fetchTopHeadlines(UserPreferences userPreferences);
}
