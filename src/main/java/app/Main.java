package app;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addTopHeadlinesView()
                .addTopHeadlinesUseCase()
                .addLoginUseCase()
                .addSignupUseCase()
                .addSaveArticleUseCase()
                .addSearchNewsUseCase()
                .addDiscoverPageView()
                .addCredibilityUseCases()
                .addFilterCredibilityUseCase()
                .addDiscoverPageUseCase()
                .addProfileView()
                .addProfileUseCase()
                .addLoadSavedArticlesView()
                .addLoadSavedArticlesUseCase()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
