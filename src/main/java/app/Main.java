package app;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addTopHeadlinesView()
                .addDiscoverPageView()
                .addTopHeadlinesUseCase()
                .addSaveArticleUseCase()
                .addSearchNewsUseCase()
                .addFilterNewsUseCase()
                .addDiscoverPageUseCase()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
