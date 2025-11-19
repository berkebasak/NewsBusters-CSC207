package use_case.set_preferences;

public class SetPreferencesOutputData {
    private final boolean success;
    private final String message;

    public SetPreferencesOutputData(boolean success, String message) {
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
