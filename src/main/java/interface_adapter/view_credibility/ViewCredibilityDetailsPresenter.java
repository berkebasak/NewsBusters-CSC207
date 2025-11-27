package interface_adapter.view_credibility;

import use_case.view_credibility.ViewCredibilityDetailsOutputBoundary;
import use_case.view_credibility.ViewCredibilityDetailsOutputData;

public class ViewCredibilityDetailsPresenter implements ViewCredibilityDetailsOutputBoundary {

    private final ViewCredibilityDetailsViewModel viewModel;

    public ViewCredibilityDetailsPresenter(ViewCredibilityDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewCredibilityDetailsOutputData outputData) {
        ViewCredibilityDetailsState state = viewModel.getState();

        state.setTitle(outputData.getTitle());
        state.setSource(outputData.getSource());
        state.setUrl(outputData.getUrl());

        state.setSourceScore(outputData.getSourceScore());
        state.setSentimentScore(outputData.getSentimentScore());
        state.setClaimConfidence(outputData.getClaimConfidence());
        state.setOverallTrust(outputData.getOverallTrust());
        state.setLevel(outputData.getLevel());
        state.setRationale(outputData.getRationale());

        state.setWeightSource(outputData.getWeightSource());
        state.setWeightSentiment(outputData.getWeightSentiment());
        state.setWeightClaim(outputData.getWeightClaim());

        state.setError(null);

        viewModel.firePropertyChange("details");
    }

    @Override
    public void prepareFailView(String message) {
        ViewCredibilityDetailsState state = viewModel.getState();
        state.setError(message);
        viewModel.firePropertyChange("error");
    }
}
