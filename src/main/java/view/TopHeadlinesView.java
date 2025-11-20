package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.top_headlines.TopHeadlinesController;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.search_news.SearchNewsController;
import interface_adapter.save_article.SaveArticleController;
import interface_adapter.save_article.SaveArticleViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.net.URI;

public class TopHeadlinesView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "top_headlines_view";
    private TopHeadlinesController controller;
    private ProfileController profileController;
    private final TopHeadlinesViewModel viewModel;
    private SaveArticleController saveController;
    private SaveArticleViewModel saveViewModel;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Load Top Headlines");
    private final JButton saveButton = new JButton("Save Article");
    private final JButton discoverButton = new JButton("Discover Page");
    private final JButton profileButton = new JButton("Profile");

    private SearchNewsController searchNewsController;
    private final JTextField keywordField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");

    public TopHeadlinesView(TopHeadlinesController controller, TopHeadlinesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.saveController = null;
        this.saveViewModel = null;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(profileButton);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel title = new JLabel("Top News Headlines");
        title.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        controlsPanel.add(title);
        controlsPanel.add(refreshButton);
        controlsPanel.add(saveButton);
        controlsPanel.add(discoverButton);
        controlsPanel.setBackground(Color.WHITE);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        headerPanel.add(controlsPanel, BorderLayout.CENTER);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchBar.add(new JLabel("Keyword:"));
        searchBar.add(keywordField);
        searchBar.add(searchButton);
        searchBar.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(headerPanel);
        topPanel.add(searchBar);
        topPanel.setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);

        articleList.setCellRenderer(new ArticleRenderer());
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articleList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadArticles());

        saveButton.addActionListener(e -> {
            Article article = articleList.getSelectedValue();
            if (article == null) {
                JOptionPane.showMessageDialog(this, "Please select an article first.");
                return;
            }
            if (saveController == null) {
                JOptionPane.showMessageDialog(this, "Unable to save article.");
                return;
            }
            saveController.save(article);
        });

        discoverButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(DiscoverPageView.VIEW_NAME);
            }
        });

        searchButton.addActionListener(e -> {
            if (searchNewsController != null) {
                String keyword = keywordField.getText().trim();
                if (!keyword.isEmpty()) {
                    searchNewsController.excute(keyword);
                }
            }
        });

        articleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Article article = articleList.getSelectedValue();
                    if (article != null) {
                        openInBrowser(article.getUrl());
                        if (profileController != null && loginViewModel != null) {
                            String username = loginViewModel.getState().getUsername();
                            profileController.addHistory(username, article);
                        }
                    }
                }
            }
        });
    }

    public void setController(TopHeadlinesController controller) {
        this.controller = controller;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setSaveArticleUseCase(SaveArticleController controller,
            SaveArticleViewModel viewModel) {
        this.saveController = controller;
        this.saveViewModel = viewModel;

        this.saveViewModel.addPropertyChangeListener(evt -> {
            if ("message".equals(evt.getPropertyName())) {
                String msg = (String) evt.getNewValue();
                if (msg != null && !msg.isEmpty()) {
                    JOptionPane.showMessageDialog(this, msg);
                }
            }
        });
    }

    public void setSearchNewsController(SearchNewsController searchNewsController) {
        this.searchNewsController = searchNewsController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel, LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;

        profileButton.addActionListener(e -> {
            if (profileController != null && loginViewModel != null) {
                String username = loginViewModel.getState().getUsername();
                profileController.execute(username);
            }
        });
    }

    private void loadArticles() {
        controller.fetchHeadlines();
        listModel.clear();
        for (Article a : viewModel.getState().getArticles()) {
            listModel.addElement(a);
        }
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listModel.clear();

        var state = viewModel.getState();

        java.util.List<Article> articles = state.getArticles();
        if (articles != null) {
            for (Article a : articles) {
                listModel.addElement(a);
            }
        }

        String error = state.getError();
        if (error != null && !error.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    error,
                    "Search News",
                    JOptionPane.INFORMATION_MESSAGE);
            state.setError(null);
        }
    }

    static class ArticleRenderer extends JPanel implements ListCellRenderer<Article> {
        private final JLabel titleLabel = new JLabel();
        private final JLabel sourceLabel = new JLabel();

        public ArticleRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.WHITE);

            titleLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 16));
            titleLabel.setForeground(Color.BLACK);

            sourceLabel.setFont(new Font("TimesNewRoman", Font.ITALIC, 13));
            sourceLabel.setForeground(new Color(100, 100, 100));

            add(titleLabel, BorderLayout.CENTER);
            add(sourceLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Article> list, Article article, int index,
                boolean isSelected, boolean cellHasFocus) {

            titleLabel.setText(article.getTitle());
            sourceLabel.setText("Source: " + article.getSource());

            if (isSelected) {
                setBackground(new Color(225, 235, 255));
            } else {
                setBackground(Color.WHITE);
            }

            return this;
        }
    }

}
