package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {
    private final String username;
    private String password;
    private final List<Article> savedArticles;
    private final List<Article> history;

    private User(String username, String password, List<Article> savedArticles) {
        this.username = Objects.requireNonNull(username, "username").trim();
        this.password = Objects.requireNonNull(password, "password");
        this.savedArticles = new ArrayList<>(savedArticles == null ? List.of() : savedArticles);
        this.history = new ArrayList<>();
    }

    private User(String username, String password, List<Article> savedArticles, List<Article> history) {
        this.username = Objects.requireNonNull(username, "username").trim();
        this.password = Objects.requireNonNull(password, "password");
        this.savedArticles = new ArrayList<>(savedArticles == null ? List.of() : savedArticles);
        this.history = new ArrayList<>(history == null ? List.of() : history);
    }

    public static User create(String username, String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        return new User(username, plainPassword, new ArrayList<>(), new ArrayList<>());
    }

    public static User fromPersistence(String username, String password, List<Article> savedArticles,
            List<Article> history) {
        return new User(username, password, savedArticles, history);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPlainPassword) {
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        this.password = newPlainPassword;
    }

    public boolean passwordMatches(String plainPassword) {
        if (plainPassword == null) {
            return false;
        }
        return password.equals(plainPassword);
    }

    public List<Article> getSavedArticles() {
        return Collections.unmodifiableList(savedArticles);
    }

    public boolean addSavedArticle(Article article) {
        if (article == null) {
            return false;
        }
        boolean alreadySaved = savedArticles.stream()
                .anyMatch(existing -> Objects.equals(existing.getUrl(), article.getUrl())
                        && Objects.equals(existing.getTitle(), article.getTitle()));
        if (alreadySaved) {
            return false;
        }
        return savedArticles.add(article);
    }

    public boolean removeSavedArticleByUrl(String url) {
        if (url == null) {
            return false;
        }
        return savedArticles.removeIf(article -> url.equals(article.getUrl()));
    }

    public List<Article> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void addToHistory(Article article) {
        if (article != null) {

            history.removeIf(a -> a.getUrl().equals(article.getUrl()));

            article.setAccessedAt(java.time.LocalDateTime.now());

            history.add(0, article);
            // Limit history size to, say, 20
            if (history.size() > 20) {
                history.remove(history.size() - 1);
            }
        }
    }

}
