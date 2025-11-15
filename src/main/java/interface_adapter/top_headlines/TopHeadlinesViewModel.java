package interface_adapter.top_headlines;

import interface_adapter.ViewModel;
import interface_adapter.search_news.SearchNewsState;

public class TopHeadlinesViewModel extends ViewModel<TopHeadlinesState> {

    public TopHeadlinesViewModel() {
        super("top headlines");
        setState(new TopHeadlinesState());
    }
}
