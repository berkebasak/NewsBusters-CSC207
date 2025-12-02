package interface_adapter.save_article;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The {@code SaveArticleViewModel} stores the state required by the view when displaying
 * information related to saving an article. It notifies registered listeners whenever
 * its state changes so that the UI can update accordingly.
 */
public class SaveArticleViewModel {

    /**
     * Provides support for managing property change listeners and firing change events.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * A message that indicates the status of an article save operation,
     * such as success or error feedback.
     */
    private String message = "";

    /**
     * Returns the current message stored in the view model.
     *
     * @return the status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message value and notifies listeners that the message has changed.
     *
     * @param message the new status message
     */
    public void setMessage(String message) {
        this.message = message;
        support.firePropertyChange("message", null, message);
    }

    /**
     * Registers a listener to be notified when properties of this view model change.
     *
     * @param listener the listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

