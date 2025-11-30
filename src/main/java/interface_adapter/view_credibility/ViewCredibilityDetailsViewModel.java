package interface_adapter.view_credibility;

import interface_adapter.ViewModel;

public class ViewCredibilityDetailsViewModel extends ViewModel<ViewCredibilityDetailsState> {

    public static final String VIEW_NAME = "credibility_details";

    public ViewCredibilityDetailsViewModel() {
        super(VIEW_NAME);
        setState(new ViewCredibilityDetailsState());
    }
}
