package use_case.top_headlines;

public class TopHeadlinesInputData {
    // This can later include parameters like category or country
    private final String requestType;

    public TopHeadlinesInputData(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
}
