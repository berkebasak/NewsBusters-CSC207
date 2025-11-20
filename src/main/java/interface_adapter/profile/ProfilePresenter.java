package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import use_case.profile.ProfileOutputBoundary;
import use_case.profile.ProfileOutputData;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public ProfilePresenter(ProfileViewModel profileViewModel, ViewManagerModel viewManagerModel) {
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ProfileOutputData outputData) {
        ProfileState state = profileViewModel.getState();
        state.setUsername(outputData.getUsername());
        state.setHistory(outputData.getHistory());
        profileViewModel.setState(state);
        profileViewModel.firePropertyChange();

        viewManagerModel.changeView(profileViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ProfileState state = profileViewModel.getState();
        state.setError(error);
        profileViewModel.firePropertyChange();
    }
}
