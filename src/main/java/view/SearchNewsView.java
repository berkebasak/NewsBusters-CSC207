package view;

import interface_adapter.search_news.SearchNewsController;
import interface_adapter.search_news.SearchNewsViewModel;
import interface_adapter.ViewManagerModel;

import entity.Article;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class SearchNewsView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "search news"; // or "searchNews"

    private final SearchNewsController controller;
    private final SearchNewsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private final JTextField keywordField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JButton backButton = new JButton("Back to Top Headlines");

    private final JLabel errorLabel = new JLabel();
    private final DefaultListModel<String> articlesListModel = new DefaultListModel<>();
    private final JList<String> articlesList = new JList<>(articlesListModel);

    public SearchNewsView(SearchNewsController controller,
                          SearchNewsViewModel viewModel,
                          ViewManagerModel viewManagerModel) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Top panel: title + back button
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Search News by Keyword");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(backButton, BorderLayout.EAST);

        // Middle panel: search bar
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Keyword: "));
        searchPanel.add(keywordField);
        searchPanel.add(searchButton);

        // Error label
        errorLabel.setForeground(Color.RED);

        // Articles list
        JScrollPane scrollPane = new JScrollPane(articlesList);

        add(topPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST); // or use a different layout if you prefer

        // Listeners
        searchButton.addActionListener(e -> {
            String keyword = keywordField.getText();
            controller.execute(keyword);   // ✅ always call, interactor handles empty
        });

        backButton.addActionListener(e -> {
            // Switch back to top headlines view
            viewManagerModel.setActiveView("top headlines"); // use your actual name
            viewManagerModel.firePropertyChanged();
        });
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Called when SearchNewsViewModel.firePropertyChanged() is called

        // 1. Update error label
        errorLabel.setText(viewModel.getErrorMessage());

        // 2. Update articles list
        articlesListModel.clear();
        List<Article> articles = viewModel.getArticles();
        if (articles != null) {
            for (Article a : articles) {
                articlesListModel.addElement(a.getTitle());
            }
        }
    }
}
