package use_case.load_saved_articles;

public class LoadSavedArticlesInputData {
    private final String username;

    public LoadSavedArticlesInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
