package entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Article {
    private String id;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String source;
    private Set<String> topics;
    private String content;
    private LocalDateTime publicationDate;
    private double trustScore;
    private String confidenceLevel;
    private boolean isSaved;
    private CredibilityScore credibilityScore;
    private LocalDateTime accessedAt;

    // Constructor
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
        this.content = null;
        this.publicationDate = null;
    }

    public Article(String id,
            String title,
            String description,
            String url,
            String imageUrl,
            String source,
            String content,
            LocalDateTime publicationDate,
            Set<String> topics,
            CredibilityScore credibilityScore,
            double trustScore,
            String confidenceLevel,
            boolean isSaved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.source = source;
        this.content = content;
        this.publicationDate = publicationDate;
        this.topics = topics == null ? new HashSet<>() : new HashSet<>(topics);
        this.credibilityScore = credibilityScore;
        this.trustScore = trustScore;
        this.confidenceLevel = confidenceLevel == null ? "Unknown" : confidenceLevel;
        this.isSaved = isSaved;
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

    public String getContent() {
        return content;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
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

    public void setTopics(Set<String> topics) {
        this.topics = topics == null ? new HashSet<>() : new HashSet<>(topics);
    }

    public void addTopic(String topic) {
        topics.add(topic);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDateTime getAccessedAt() {
        return accessedAt;
    }

    public void setAccessedAt(LocalDateTime accessedAt) {
        this.accessedAt = accessedAt;
    }

    public Article copyWithSaved(boolean saved) {
        Article copy = new Article(
                id,
                title,
                description,
                url,
                imageUrl,
                source,
                content,
                publicationDate,
                topics,
                credibilityScore,
                trustScore,
                confidenceLevel,
                saved);
        copy.setAccessedAt(this.accessedAt);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Article))
            return false;
        Article article = (Article) o;
        return Objects.equals(url, article.url) && Objects.equals(title, article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }

    @Override
    public String toString() {
        return title + " (" + source + ")";
    }
}
