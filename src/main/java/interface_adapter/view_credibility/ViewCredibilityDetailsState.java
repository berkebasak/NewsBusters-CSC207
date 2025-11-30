package interface_adapter.view_credibility;

public class ViewCredibilityDetailsState {

    private String title;
    private String source;
    private String url;

    private double sourceScore;
    private double sentimentScore;
    private double claimConfidence;
    private double overallTrust;
    private String level;
    private String rationale;

    private double weightSource;
    private double weightSentiment;
    private double weightClaim;

    private String error;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getSourceScore() {
        return sourceScore;
    }

    public void setSourceScore(double sourceScore) {
        this.sourceScore = sourceScore;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public double getClaimConfidence() {
        return claimConfidence;
    }

    public void setClaimConfidence(double claimConfidence) {
        this.claimConfidence = claimConfidence;
    }

    public double getOverallTrust() {
        return overallTrust;
    }

    public void setOverallTrust(double overallTrust) {
        this.overallTrust = overallTrust;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public double getWeightSource() {
        return weightSource;
    }

    public void setWeightSource(double weightSource) {
        this.weightSource = weightSource;
    }

    public double getWeightSentiment() {
        return weightSentiment;
    }

    public void setWeightSentiment(double weightSentiment) {
        this.weightSentiment = weightSentiment;
    }

    public double getWeightClaim() {
        return weightClaim;
    }

    public void setWeightClaim(double weightClaim) {
        this.weightClaim = weightClaim;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
