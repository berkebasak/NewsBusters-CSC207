package use_case.login;

import entity.Article;

import java.util.Collections;
import java.util.List;

public class LoginOutputData {
    private final String username;
    private final List<Article> savedArticles;

    public LoginOutputData(String username, List<Article> savedArticles) {
        this.username = username;
        this.savedArticles = savedArticles == null ? List.of() : List.copyOf(savedArticles);
    }

    public String getUsername() {
        return username;
    }

    public List<Article> getSavedArticles() {
        return Collections.unmodifiableList(savedArticles);
    }
}
