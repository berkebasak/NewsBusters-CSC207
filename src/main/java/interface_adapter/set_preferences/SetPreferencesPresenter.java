package interface_adapter.set_preferences;

import entity.UserPreferences;
import interface_adapter.ViewManagerModel;
import use_case.set_preferences.SetPreferencesOutputBoundary;
import use_case.set_preferences.SetPreferencesOutputData;

public class SetPreferencesPresenter implements SetPreferencesOutputBoundary {

    private final SetPreferencesViewModel setPreferencesViewModel;

    public SetPreferencesPresenter(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
    }

    @Override
    public void initPreferenceView(UserPreferences userPreferences) {
        SetPreferencesState state = setPreferencesViewModel.getState();
        state.setUserPreferences(userPreferences);
        state.setError(null);
        state.setMessage(null);
        setPreferencesViewModel.firePropertyChange();
    }

    @Override
    public void prepareSuccessView(SetPreferencesOutputData outputData) {
        SetPreferencesState state = setPreferencesViewModel.getState();
        state.setUserPreferences(outputData.getUserPreferences());
        state.setError(null);
        state.setMessage(outputData.getMessage());
        setPreferencesViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SetPreferencesState state = setPreferencesViewModel.getState();
        state.setError(errorMessage);
        setPreferencesViewModel.firePropertyChange();
    }
}
