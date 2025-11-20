package use_case.profile;

import entity.Article;
import java.util.List;

public class ProfileOutputData {
    private final String username;
    private final List<Article> history;

    public ProfileOutputData(String username, List<Article> history) {
        this.username = username;
        this.history = history;
    }

    public String getUsername() {
        return username;
    }

    public List<Article> getHistory() {
        return history;
    }
}
