package interface_adapter.search_news;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for the Search News feature.
 */

public class SearchNewsViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SearchNewsState state = new SearchNewsState();

    /**
     * @return the current state object
     */
    public SearchNewsState getState() {
        return state;
    }

    /**
     * Replace the current state with a new one.
     * @param newState new state from the Presenter
     */
    public void setState(SearchNewsState newState) {
        this.state = newState;
    }

    /**
     * Tells listeners that the state changed.
     * @param propertyName the label for the property that was changed
     */
    public void firePropertyChanged(String propertyName) {
        this.support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Add a UI listener to receive updates.
     * @param listener a PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
