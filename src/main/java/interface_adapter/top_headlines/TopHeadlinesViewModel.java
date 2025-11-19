package interface_adapter.top_headlines;

import interface_adapter.ViewModel;

public class TopHeadlinesViewModel extends ViewModel<TopHeadlinesState> {

    public static final String VIEW_NAME = "top_headlines_view";

    public TopHeadlinesViewModel() {
        super(VIEW_NAME);
        setState(new TopHeadlinesState());
    }
}
