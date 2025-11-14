package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A generic ViewModel used by all ViewModel subclasses.
 * @param <T> the type of the state object
 */
public class ViewModel<T> {

    private final String viewName;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private T state;

    /**
     * Creates a ViewModel with the given view name.
     * @param viewName the name connected with this View
     */
    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    /**
     * @return the name of the view that this ViewModel represents
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * @return the current state stored in this ViewModel
     */
    public T getState() {
        return this.state;
    }

    /**
     * Replace the current state with a new state.
     *
     * @param state the new state object
     */
    public void setState(T state) {
        this.state = state;
    }

    /**
     * Fires a property changed event for the state property.
     */
    public void firePropertyChange() {
        this.support.firePropertyChange("state", null, this.state);
    }

    /**
     * Fires a property changed event with a custom property name.
     * @param propertyName the property that changed
     */
    public void firePropertyChange(String propertyName) {
        this.support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Adds a View as a listener to this ViewModel.
     * @param listener the PropertyChangeListener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
