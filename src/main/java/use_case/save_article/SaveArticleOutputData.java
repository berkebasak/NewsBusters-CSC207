package use_case.save_article;

public class SaveArticleOutputData {
    private final boolean success;
    private final String message;

    public SaveArticleOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
