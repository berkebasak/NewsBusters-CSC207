package interface_adapter.generate_credibility;

import entity.Article;
import interface_adapter.discover_page.DiscoverPageState;
import interface_adapter.discover_page.DiscoverPageViewModel;
import use_case.generate_credibility.GenerateCredibilityOutputBoundary;
import use_case.generate_credibility.GenerateCredibilityOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscoverGenerateCredibilityPresenter implements GenerateCredibilityOutputBoundary {

    private final DiscoverPageViewModel discoverPageViewModel;

    public DiscoverGenerateCredibilityPresenter(DiscoverPageViewModel discoverPageViewModel) {
        this.discoverPageViewModel = discoverPageViewModel;
    }

    @Override
    public void prepareSuccessView(GenerateCredibilityOutputData outputData) {
        DiscoverPageState state = discoverPageViewModel.getState();

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
        state.setMessage(null);
        state.setHasNoArticles(false);
        state.setHasNoHistory(false);
        discoverPageViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String message) {
        DiscoverPageState state = discoverPageViewModel.getState();
        state.setMessage(message);
        discoverPageViewModel.firePropertyChange();
    }
}
