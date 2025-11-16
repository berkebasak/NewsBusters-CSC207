package use_case.top_headlines;

public class TopHeadlinesInputData {

    private final String requestType;

    public TopHeadlinesInputData(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
}
