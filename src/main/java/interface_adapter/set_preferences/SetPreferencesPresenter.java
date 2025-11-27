package interface_adapter.set_preferences;

import use_case.set_preferences.SetPreferencesOutputBoundary;
import use_case.set_preferences.SetPreferencesOutputData;

public class SetPreferencesPresenter implements SetPreferencesOutputBoundary {

    private final SetPreferencesViewModel setPreferencesViewModel;

    public SetPreferencesPresenter(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
    }

    @Override
    public void prepareSuccessView(SetPreferencesOutputData outputData) {
        SetPreferencesState state = setPreferencesViewModel.getState();
        state.setUserPreferences(outputData.getUserPreferences());
        state.setError(null);
        setPreferencesViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SetPreferencesState state = setPreferencesViewModel.getState();
        state.setError(errorMessage);
        setPreferencesViewModel.firePropertyChange();
    }
}
