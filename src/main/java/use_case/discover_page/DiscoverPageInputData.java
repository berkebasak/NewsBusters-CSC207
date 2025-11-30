package use_case.discover_page;

import java.util.Set;

public class DiscoverPageInputData {
    private final Set<String> currentTopics;
    private final int currentPage;

    public DiscoverPageInputData(Set<String> currentTopics, int currentPage) {
        this.currentTopics = currentTopics;
        this.currentPage = currentPage;
    }

    public Set<String> getCurrentTopics() {
        return currentTopics;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
