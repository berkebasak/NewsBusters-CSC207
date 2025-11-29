package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.load_saved_articles.LoadSavedArticlesState;
import interface_adapter.load_saved_articles.LoadSavedArticlesViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

public class LoadSavedArticlesView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = LoadSavedArticlesViewModel.VIEW_NAME;

    private final LoadSavedArticlesViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;

    private final JLabel usernameLabel = new JLabel();

    private final DefaultListModel<Article> savedListModel = new DefaultListModel<>();
    private final JList<Article> savedList = new JList<>(savedListModel);

    private final JButton backButton = new JButton("Back");

    public LoadSavedArticlesView(LoadSavedArticlesViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Saved Articles");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(title, BorderLayout.WEST);

        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(Color.WHITE);
        headerRight.add(usernameLabel);
        headerRight.add(backButton);
        headerPanel.add(headerRight, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // List + ScrollPane
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(0, 20, 20, 20),
                        BorderFactory.createTitledBorder("Your Saved Articles")
                )
        );

        savedList.setCellRenderer(new ArticleRenderer());
        savedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(savedList);  // ✅ scrollable list
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        add(listPanel, BorderLayout.CENTER);

        // Listeners
        backButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(ProfileViewModel.VIEW_NAME);
                viewManagerModel.firePropertyChange();
            }
        });

        savedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double click
                    Article article = savedList.getSelectedValue();
                    if (article != null) {
                        openInBrowser(article.getUrl());
                    }
                }
            }
        });
    }

    // helper for AppBuilder
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    private void openInBrowser(String url) {
        if (url == null || url.isBlank()) {
            JOptionPane.showMessageDialog(this, "No URL available for this article.");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoadSavedArticlesState state = viewModel.getState();
        usernameLabel.setText("User: " + state.getUsername());

        savedListModel.clear();
        for (Article article : state.getSavedArticles()) {
            savedListModel.addElement(article);
        }

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError());
            state.setError(null);
        }
    }

    // Simple renderer for the list items
    static class ArticleRenderer extends JPanel implements ListCellRenderer<Article> {
        private final JLabel titleLabel = new JLabel();
        private final JLabel sourceLabel = new JLabel();

        public ArticleRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setBackground(Color.WHITE);

            titleLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
            sourceLabel.setFont(new Font("TimesNewRoman", Font.ITALIC, 12));
            sourceLabel.setForeground(Color.GRAY);

            add(titleLabel, BorderLayout.CENTER);
            add(sourceLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Article> list,
                Article value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            titleLabel.setText(value.getTitle());
            String src = value.getSource() == null ? "" : value.getSource();
            sourceLabel.setText(src);

            if (isSelected) {
                setBackground(new Color(225, 235, 255));
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
