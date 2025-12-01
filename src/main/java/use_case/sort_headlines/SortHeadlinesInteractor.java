package use_case.sort_headlines;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class SortHeadlinesInteractor implements SortHeadlinesInputBoundary {
    private final SortHeadlinesOutputBoundary presenter;

    public SortHeadlinesInteractor(SortHeadlinesOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(SortHeadlinesInputData inputData) {
        List<Article> articles = inputData.getArticles();
        String order = inputData.getSortOrder();

        if (articles == null || articles.isEmpty()) {
            presenter.prepareFailView("No articles to sort.");
            return;
        }

        // Check if trust scores are available
        boolean missingScores = false;
        for (Article article : articles) {
            if (article.getCredibilityScore() == null) {
                missingScores = true;
                break;
            }
        }

        if (missingScores) {
            presenter.prepareFailView("Sorting unavailable - missing trust scores.");
            return;
        }

        List<Article> sortedArticles = new ArrayList<>(articles);
        if ("high".equalsIgnoreCase(order)) {
            sortedArticles.sort((a1, a2) -> Double.compare(a2.getTrustScore(), a1.getTrustScore()));
        } else if ("low".equalsIgnoreCase(order)) {
            sortedArticles.sort((a1, a2) -> Double.compare(a1.getTrustScore(), a2.getTrustScore()));
        }

        presenter.prepareSuccessView(new SortHeadlinesOutputData(sortedArticles));
    }
}
