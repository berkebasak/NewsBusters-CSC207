package entity;

import java.util.ArrayList;
import java.util.Set;

public class UserPreferences {
    private final ArrayList<String> preferredTopics;
    private final ArrayList<String> blockedSources;
    private String language;
    private String region;

    public UserPreferences() {
        this.preferredTopics = new ArrayList<>();
        this.blockedSources = new ArrayList<>();
        this.language = "English";
        this.region = "Canada";
    }

    public UserPreferences(ArrayList<String> preferredTopics, ArrayList<String> blockedSources,
                           String language, String region) {
        this.preferredTopics = preferredTopics;
        this.blockedSources = blockedSources;
        this.language = language;
        this.region = region;
    }

    public ArrayList<String> getPreferredTopics() {
        return preferredTopics;
    }

    public void addPreferredTopic(String newPreferredTopic) {
        this.preferredTopics.add(newPreferredTopic);
    }

    public void removePreferredTopic(String preferredTopicToRemove) {
        this.preferredTopics.remove(preferredTopicToRemove);
    }

    public ArrayList<String> getBlockedSources() {
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
