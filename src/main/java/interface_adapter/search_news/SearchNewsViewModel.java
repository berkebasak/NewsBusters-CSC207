package interface_adapter.search_news;

import interface_adapter.ViewModel;
import interface_adapter.top_headlines.TopHeadlinesState;

/**
 * ViewModel for the Search News use case.
 */
public class SearchNewsViewModel extends ViewModel<SearchNewsState> {

    public static final String VIEW_NAME = "search_news_view";

    public SearchNewsViewModel() {
        super(VIEW_NAME);
        setState(new SearchNewsState());
    }
}