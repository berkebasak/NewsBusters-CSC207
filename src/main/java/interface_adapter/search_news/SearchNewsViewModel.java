package interface_adapter.search_news;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Search News feature.
 */
public class SearchNewsViewModel extends ViewModel<SearchNewsState> {

    // Labels for the UI
    public static final String TITLE_LABEL = "Search News";
    public static final String KEYWORD_LABEL = "Keyword:";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final String VIEW_NAME = "search news";

    public SearchNewsViewModel() {
        super(VIEW_NAME);
        setState(new SearchNewsState());
    }
}
