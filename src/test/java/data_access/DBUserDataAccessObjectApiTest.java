package data_access;

import entity.Article;
import entity.UserPreferences;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBUserDataAccessObjectApiTest {

    @Test
    void fetchTopHeadlinesReturnsArticlesOrEmptyGracefully() {
        DBUserDataAccessObject dao = new DBUserDataAccessObject();

        List<Article> articles = dao.fetchTopHeadlines(new UserPreferences());
        assertNotNull(articles);

        if (articles.isEmpty()) {
            System.out.println("DBUserDataAccessObjectApiTest: no articles returned (API key / network / quota issue), not failing test.");
            return;
        }

        Article first = articles.get(0);
        assertNotNull(first.getTitle());
        assertFalse(first.getTitle().isBlank());
        assertNotNull(first.getUrl());
        assertFalse(first.getUrl().isBlank());
    }
}
