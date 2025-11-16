package interface_adapter.discover_page;

import interface_adapter.ViewModel;

public class DiscoverPageViewModel extends ViewModel<DiscoverPageState> {

    public DiscoverPageViewModel() {
        super("discover page");
        setState(new DiscoverPageState());
    }
}

