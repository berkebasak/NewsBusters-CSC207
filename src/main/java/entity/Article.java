package entity;

import java.util.HashSet;
import java.util.Set;

public class Article {
    private String id;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String source;
    private Set<String> topics;
    private double trustScore;
    private String confidenceLevel;
    private boolean isSaved;
    private CredibilityScore credibilityScore;

    //Constructor
    public Article(String id, String title, String description, String url, String imageUrl, String source) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.source = source;
        this.topics = new HashSet<>();
        this.trustScore = 0.0;
        this.confidenceLevel = "Unknown";
        this.isSaved = false;
        this.credibilityScore = null;
    }

    // getter and setter methods
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return url;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getSource() {
        return source;
    }
    public Set<String> getTopics() {
        return topics;
    }
    public double getTrustScore() {
        return trustScore;
    }
    public String getConfidenceLevel() {
        return confidenceLevel;
    }
    public boolean isSaved() {
        return isSaved;
    }
    public CredibilityScore getCredibilityScore() {
        return credibilityScore;
    }

    public void setTrustScore(double trustScore) {
        this.trustScore = trustScore;
    }
    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    public void setSaved(boolean saved) {
        isSaved = saved;
    }
    public void setCredibilityScore(CredibilityScore credibilityScore) {
        this.credibilityScore = credibilityScore;
    }
    public void addTopic(String topic) {
        topics.add(topic);
    }

    @Override
    public String toString() {
        return title + " (" + source + ")";
    }
}
