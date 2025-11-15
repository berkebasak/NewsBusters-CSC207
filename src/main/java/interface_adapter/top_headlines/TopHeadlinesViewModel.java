package interface_adapter.top_headlines;

import interface_adapter.ViewModel;

public class TopHeadlinesViewModel extends ViewModel<TopHeadlinesState> {

    public TopHeadlinesViewModel() {
        super("top headlines");
        setState(new TopHeadlinesState());
    }
}
