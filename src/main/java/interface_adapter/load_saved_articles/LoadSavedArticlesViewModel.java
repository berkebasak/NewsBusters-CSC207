package interface_adapter.load_saved_articles;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import interface_adapter.ViewModel;

public class LoadSavedArticlesViewModel extends ViewModel<LoadSavedArticlesState> {

    public static final String VIEW_NAME = "load_saved_articles";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public LoadSavedArticlesViewModel() {
        super(VIEW_NAME);
        setState(new LoadSavedArticlesState());
    }

    @Override
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.getState());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
