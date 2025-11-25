package interface_adapter.view_credibility;

import entity.Article;
import use_case.view_credibility.ViewCredibilityDetailsInputBoundary;
import use_case.view_credibility.ViewCredibilityDetailsInputData;

public class ViewCredibilityDetailsController {

    private final ViewCredibilityDetailsInputBoundary interactor;

    public ViewCredibilityDetailsController(ViewCredibilityDetailsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showDetails(Article article) {
        ViewCredibilityDetailsInputData inputData =
                new ViewCredibilityDetailsInputData(article);
        interactor.execute(inputData);
    }
}
