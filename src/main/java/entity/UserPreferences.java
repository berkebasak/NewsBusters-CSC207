package entity;

import java.util.HashSet;
import java.util.Set;

public class UserPreferences {
    private final HashSet<String> preferredTopics;
    private final HashSet<String> blockedSources;
    private String language;
    private String region;

    public UserPreferences(HashSet<String> preferredTopics, HashSet<String> blockedSources,
                           String language, String region) {
        this.preferredTopics = preferredTopics;
        this.blockedSources = blockedSources;
        this.language = language;
        this.region = region;
    }

    public Set<String> getPreferredTopics() {
        return preferredTopics;
    }

    public void addPreferredTopic(String newPreferredTopic) {
        this.preferredTopics.add(newPreferredTopic);
    }

    public void removePreferredTopic(String preferredTopicToRemove) {
        this.preferredTopics.remove(preferredTopicToRemove);
    }

    public Set<String> getBlockedSources() {
        return blockedSources;
    }

    public void addBlockedSource(String newBlockedSource) {
        this.blockedSources.add(newBlockedSource);
    }

    public void removeBlockedSource(String blockedSourceToRemove) {
        this.blockedSources.remove(blockedSourceToRemove);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
