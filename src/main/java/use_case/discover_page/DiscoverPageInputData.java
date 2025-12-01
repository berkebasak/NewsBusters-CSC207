package use_case.discover_page;

import java.util.Set;

public class DiscoverPageInputData {
    private final Set<String> currentTopics;
    private final int currentPage;
    private final String username;

    public DiscoverPageInputData(Set<String> currentTopics, int currentPage, String username) {
        this.currentTopics = currentTopics;
        this.currentPage = currentPage;
        this.username = username;
    }

    public Set<String> getCurrentTopics() {
        return currentTopics;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getUsername() {
        return username;
    }
}
