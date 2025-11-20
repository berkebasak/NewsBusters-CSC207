package use_case.profile;

import entity.Article;

public interface ProfileInputBoundary {
    void execute(ProfileInputData inputData);

    void addHistory(String username, Article article);
}
