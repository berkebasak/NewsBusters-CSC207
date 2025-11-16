package interface_adapter.discover_page;

import interface_adapter.ViewModel;

public class DiscoverPageViewModel extends ViewModel<DiscoverPageState> {

    public static final String VIEW_NAME = "discover_page_view";

    public DiscoverPageViewModel() {
        super("discover page");
        setState(new DiscoverPageState());
    }
}

