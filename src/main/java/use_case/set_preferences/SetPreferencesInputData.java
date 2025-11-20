package use_case.set_preferences;

import java.util.ArrayList;

public class SetPreferencesInputData {
    private final ArrayList<String> preferredTopics;
    private final ArrayList<String> blockedSources;
    private String language;
    private String region;

    public SetPreferencesInputData(ArrayList<String> preferredTopics, ArrayList<String> blockedSources,
                           String language, String region) {
        this.preferredTopics = preferredTopics;
        this.blockedSources = blockedSources;
        this.language = language;
        this.region = region;
    }

    public ArrayList<String> getPreferredTopics() {
        return preferredTopics;
    }

    public ArrayList<String> getBlockedSources() {
        return blockedSources;
    }

    public String getLanguage() {
        return language;
    }

    public String getRegion() {
        return region;
    }
}
