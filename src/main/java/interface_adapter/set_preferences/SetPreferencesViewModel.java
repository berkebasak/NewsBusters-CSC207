package interface_adapter.set_preferences;

import interface_adapter.ViewModel;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {

    public static final String VIEW_NAME = "set_preferences_view";

    public SetPreferencesViewModel() {
        super(VIEW_NAME);
        setState(new SetPreferencesState());
    }
}
