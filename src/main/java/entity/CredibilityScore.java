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
        double effectiveSource = sourceScore;
        if (effectiveSource < 0.0) effectiveSource = 0.0;
        if (effectiveSource > 1.0) effectiveSource = 1.0;
        double SOURCE_REF = 0.7;
        effectiveSource = effectiveSource / SOURCE_REF;
        if (effectiveSource > 1.0) effectiveSource = 1.0;

        double textScore = 0.5 * sentimentScore + 0.5 * claimConfidence;
        if (textScore < 0.0) textScore = 0.0;
        if (textScore > 1.0) textScore = 1.0;

        double effectiveText = Math.pow(textScore, 0.85);
        if (effectiveText > 1.0) effectiveText = 1.0;

        double trust = 0.7 * effectiveSource + 0.3 * effectiveText;

        if (trust < 0.0) trust = 0.0;
        if (trust > 1.0) trust = 1.0;
        return trust;
    }

    private String determineLevel() {
        if (overallTrust >= 0.8) return "High";
        else if (overallTrust >= 0.65) return "Medium";
        else return "Low";
    }

    private String generateRationale() {
        if (overallTrust >= 0.8) return "Highly reliable source and consistent sentiment.";
        else if (overallTrust >= 0.65) return "Moderate reliability with balanced indicators.";
        else return "Low reliability due to weak signals or poor source score.";
    }

    public double getSourceScore() { return sourceScore; }
    public double getSentimentScore() { return sentimentScore; }
    public double getClaimConfidence() { return claimConfidence; }
    public double getOverallTrust() { return overallTrust; }
    public String getRationale() { return rationale; }
    public String getLevel() { return level; }
}
