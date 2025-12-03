package interface_adapter.set_preferences;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {

    public static final String VIEW_NAME = "set_preferences_view";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SetPreferencesViewModel() {
        super(VIEW_NAME);
        setState(new SetPreferencesState());
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
