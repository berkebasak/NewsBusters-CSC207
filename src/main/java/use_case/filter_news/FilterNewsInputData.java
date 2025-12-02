package use_case.filter_news;

import java.util.List;

/**
 * Input data for the Filter News use case.
 * Holds the list of topics the user selected.
 */
public class FilterNewsInputData {

    private final List<String> topics;

    /**
     * Creates a new FilterNewsInputData object.
     * @param topics the topics chosen by the user
     */
    public FilterNewsInputData(List<String> topics) {
        this.topics = topics;
    }

    /**
     * Returns a list of topics selected by user for filtering
     * @return the list of selected topics
     */
    public List<String> getTopics() {
        return topics;
    }
}
