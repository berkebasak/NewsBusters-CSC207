package interface_adapter.profile;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProfileViewModel extends ViewModel<ProfileState> {
    public static final String VIEW_NAME = "profile";

    public ProfileViewModel() {
        super(VIEW_NAME);
        setState(new ProfileState());
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
