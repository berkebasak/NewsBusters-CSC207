package interface_adapter.save_article;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SaveArticleViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        String old  = this.message;
        this.message = message;
        support.firePropertyChange("message", null, message);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
