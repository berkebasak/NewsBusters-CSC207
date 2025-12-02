package interface_adapter.set_preferences;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {

    public static final String VIEW_NAME = "set_preferences_view";

    public SetPreferencesViewModel() {
        super(VIEW_NAME);
        setState(new SetPreferencesState());
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.getState());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
