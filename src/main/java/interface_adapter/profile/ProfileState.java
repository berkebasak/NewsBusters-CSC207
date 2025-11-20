package interface_adapter.profile;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class ProfileState {
    private String username = "";
    private List<Article> history = new ArrayList<>();
    private String error = null;

    public ProfileState(ProfileState copy) {
        username = copy.username;
        history = new ArrayList<>(copy.history);
        error = copy.error;
    }

    public ProfileState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Article> getHistory() {
        return history;
    }

    public void setHistory(List<Article> history) {
        this.history = history;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
