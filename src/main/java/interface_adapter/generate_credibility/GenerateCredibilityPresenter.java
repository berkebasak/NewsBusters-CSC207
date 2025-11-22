package interface_adapter.generate_credibility;

import entity.Article;
import interface_adapter.top_headlines.TopHeadlinesState;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import use_case.generate_credibility.GenerateCredibilityOutputBoundary;
import use_case.generate_credibility.GenerateCredibilityOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateCredibilityPresenter implements GenerateCredibilityOutputBoundary {

    private final TopHeadlinesViewModel topHeadlinesViewModel;

    public GenerateCredibilityPresenter(TopHeadlinesViewModel topHeadlinesViewModel) {
        this.topHeadlinesViewModel = topHeadlinesViewModel;
    }

    @Override
    public void prepareSuccessView(GenerateCredibilityOutputData outputData) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();

        List<Article> current = state.getArticles();
        if (current == null) {
            current = new ArrayList<>();
        } else {
            current = new ArrayList<>(current);
        }

        List<Article> updatedArticles = outputData.getArticles();
        if (updatedArticles != null) {
            for (Article updated : updatedArticles) {
                if (updated == null) {
                    continue;
                }

                boolean replaced = false;
                for (int i = 0; i < current.size(); i++) {
                    Article existing = current.get(i);
                    if (Objects.equals(existing.getUrl(), updated.getUrl())) {
                        current.set(i, updated);
                        replaced = true;
                        break;
                    }
                }
                if (!replaced) {
                    current.add(updated);
                }
            }
        }

        state.setArticles(current);
        state.setError(null);
        topHeadlinesViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String message) {
        TopHeadlinesState state = topHeadlinesViewModel.getState();
        state.setError(message);
        topHeadlinesViewModel.firePropertyChange();
    }
}
