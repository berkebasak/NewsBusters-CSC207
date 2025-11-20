package use_case.profile;

public interface ProfileOutputBoundary {
    void prepareSuccessView(ProfileOutputData outputData);

    void prepareFailView(String error);
}
