package interface_adapter.save_article;

import use_case.save_article.SaveArticleOutputBoundary;
import use_case.save_article.SaveArticleOutputData;

public class SaveArticlePresenter implements SaveArticleOutputBoundary {

    private final SaveArticleViewModel viewModel;

    public SaveArticlePresenter(SaveArticleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SaveArticleOutputData outputData) {
        viewModel.setMessage(outputData.getMessage());
    }
}
