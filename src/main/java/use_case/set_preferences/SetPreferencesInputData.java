package use_case.set_preferences;

import java.util.HashSet;

public class SetPreferencesInputData {
    private final HashSet<String> preferredTopics;
    private final HashSet<String> blockedSources;
    private String language;
    private String region;

    public SetPreferencesInputData(HashSet<String> preferredTopics, HashSet<String> blockedSources,
                           String language, String region) {
        this.preferredTopics = preferredTopics;
        this.blockedSources = blockedSources;
        this.language = language;
        this.region = region;
    }

    public  HashSet<String> getPreferredTopics() {
        return preferredTopics;
    }

    public  HashSet<String> getBlockedSources() {
        return blockedSources;
    }

    public String getLanguage() {
        return language;
    }

    public String getRegion() {
        return region;
    }
}
