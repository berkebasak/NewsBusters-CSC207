package interface_adapter.top_headlines;

public class TopHeadlinesViewModel {
    private final TopHeadlinesState state;

    public TopHeadlinesViewModel(TopHeadlinesState state) {
        this.state = state;
    }

    public TopHeadlinesState getState() {
        return state;
    }
}
