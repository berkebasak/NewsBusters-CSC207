package entity;

public class CredibilityScore {
    private double sourceScore;
    private double sentimentScore;
    private double claimConfidence;
    private double overallTrust;
    private String rationale;
    private String level;

    public CredibilityScore(double sourceScore, double sentimentScore, double claimConfidence) {
        this.sourceScore = sourceScore;
        this.sentimentScore = sentimentScore;
        this.claimConfidence = claimConfidence;
        this.overallTrust = calculateOverallTrust();
        this.level = determineLevel();
        this.rationale = generateRationale();
    }

    private double calculateOverallTrust() {
        return (0.5 * sourceScore + 0.3 * claimConfidence + 0.2 * sentimentScore);
    }

    private String determineLevel() {
        if (overallTrust >= 0.8) return "High";
        else if (overallTrust >= 0.5) return "Medium";
        else return "Low";
    }

    private String generateRationale() {
        if (overallTrust >= 0.8) return "Highly reliable source and consistent sentiment.";
        else if (overallTrust >= 0.5) return "Moderate reliability with balanced indicators.";
        else return "Low reliability due to weak signals or poor source score.";
    }

    public double getSourceScore() { return sourceScore; }
    public double getSentimentScore() { return sentimentScore; }
    public double getClaimConfidence() { return claimConfidence; }
    public double getOverallTrust() { return overallTrust; }
    public String getRationale() { return rationale; }
    public String getLevel() { return level; }
}
