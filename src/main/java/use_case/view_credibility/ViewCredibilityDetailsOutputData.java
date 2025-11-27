package use_case.view_credibility;

import entity.Article;

public class ViewCredibilityDetailsOutputData {

    private final String title;
    private final String source;
    private final String url;

    private final double sourceScore;
    private final double sentimentScore;
    private final double claimConfidence;
    private final double overallTrust;
    private final String level;
    private final String rationale;

    private final double weightSource;
    private final double weightSentiment;
    private final double weightClaim;

    public ViewCredibilityDetailsOutputData(String title,
                                            String source,
                                            String url,
                                            double sourceScore,
                                            double sentimentScore,
                                            double claimConfidence,
                                            double overallTrust,
                                            String level,
                                            String rationale,
                                            double weightSource,
                                            double weightSentiment,
                                            double weightClaim) {
        this.title = title;
        this.source = source;
        this.url = url;
        this.sourceScore = sourceScore;
        this.sentimentScore = sentimentScore;
        this.claimConfidence = claimConfidence;
        this.overallTrust = overallTrust;
        this.level = level;
        this.rationale = rationale;
        this.weightSource = weightSource;
        this.weightSentiment = weightSentiment;
        this.weightClaim = weightClaim;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public double getSourceScore() {
        return sourceScore;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public double getClaimConfidence() {
        return claimConfidence;
    }

    public double getOverallTrust() {
        return overallTrust;
    }

    public String getLevel() {
        return level;
    }

    public String getRationale() {
        return rationale;
    }

    public double getWeightSource() {
        return weightSource;
    }

    public double getWeightSentiment() {
        return weightSentiment;
    }

    public double getWeightClaim() {
        return weightClaim;
    }

}
