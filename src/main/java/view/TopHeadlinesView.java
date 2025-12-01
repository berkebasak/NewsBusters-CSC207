package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.top_headlines.TopHeadlinesController;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.search_news.SearchNewsController;
import interface_adapter.filter_news.FilterNewsController;
import interface_adapter.save_article.SaveArticleController;
import interface_adapter.save_article.SaveArticleViewModel;
import interface_adapter.generate_credibility.GenerateCredibilityController;
import interface_adapter.filter_credibility.FilterCredibilityController;
import interface_adapter.view_credibility.ViewCredibilityDetailsState;
import interface_adapter.view_credibility.ViewCredibilityDetailsViewModel;
import interface_adapter.view_credibility.ViewCredibilityDetailsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "top_headlines_view";

    private TopHeadlinesController controller;
    private ProfileController profileController;
    private final TopHeadlinesViewModel viewModel;

    private SaveArticleController saveController;
    private SaveArticleViewModel saveViewModel;

    private SearchNewsController searchNewsController;
    private FilterNewsController filterNewsController;
    private FilterNewsView filterNewsView;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;

    // Credibility use cases
    private GenerateCredibilityController generateCredibilityController;
    private ViewCredibilityDetailsController viewCredibilityDetailsController;
    private ViewCredibilityDetailsViewModel viewCredibilityDetailsViewModel;
    private FilterCredibilityController filterCredibilityController;

    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);

    private final JButton refreshButton = new JButton("Load Top Headlines");
    private final JButton saveButton = new JButton("Save Article");
    private final JButton discoverButton = new JButton("Discover Page");
    private final JButton profileButton = new JButton("Profile");

    // Credibility buttons
    private final JButton generateCredibilityButton = new JButton("Generate Credibility");
    private final JButton generateAllCredibilityButton = new JButton("Generate All Scores");
    private final JButton viewDetailsButton = new JButton("View Details");

    // Filter by trust score
    private final JButton filterCredibilityButton = new JButton("Filter by Credibility");

    private final JTextField keywordField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");

    private final JButton filterButton = new JButton("Filter");

    public TopHeadlinesView(TopHeadlinesController controller, TopHeadlinesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.saveController = null;
        this.saveViewModel = null;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ---------- HEADER (TITLE + BUTTONS + PROFILE) ----------

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        // Right side: profile button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(profileButton);

        // Center controls: title + main buttons + credibility buttons
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlsPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Top News Headlines");
        title.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        controlsPanel.add(title);
        controlsPanel.add(refreshButton);
        controlsPanel.add(saveButton);
        controlsPanel.add(filterButton);
        controlsPanel.add(discoverButton);
        controlsPanel.add(filterCredibilityButton);

        controlsPanel.add(generateCredibilityButton);
        controlsPanel.add(generateAllCredibilityButton);
        controlsPanel.add(viewDetailsButton);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        headerPanel.add(controlsPanel, BorderLayout.CENTER);

        // ---------- SEARCH BAR ----------

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchBar.setBackground(Color.WHITE);
        searchBar.add(new JLabel("Keyword:"));
        searchBar.add(keywordField);
        searchBar.add(searchButton);
        searchBar.add(filterButton);

        // Stack header + search bar
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(headerPanel);
        topPanel.add(searchBar);

        add(topPanel, BorderLayout.NORTH);

        // ---------- ARTICLE LIST ----------

        articleList.setCellRenderer(new ArticleRenderer());
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articleList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // ---------- BUTTON LISTENERS ----------

        // Load headlines
        refreshButton.addActionListener(e -> loadArticles());

        // Save article
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

        // Go to Discover Page
        discoverButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(DiscoverPageView.VIEW_NAME);
            }
        });

        // Search
        searchButton.addActionListener(e -> {
            if (searchNewsController != null) {
                String keyword = keywordField.getText().trim();
                if (!keyword.isEmpty()) {
                    searchNewsController.execute(keyword);
                }
            }
        });

        // Filter
        filterButton.addActionListener(e -> openFilter());

        // Double-click article to open URL and add to profile history
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

        // Generate credibility for selected article
        generateCredibilityButton.addActionListener(e -> {
            if (generateCredibilityController == null) {
                JOptionPane.showMessageDialog(this, "Credibility feature not available.");
                return;
            }
            Article article = articleList.getSelectedValue();
            if (article == null) {
                JOptionPane.showMessageDialog(this, "Please select an article first.");
                return;
            }
            generateCredibilityController.generateForArticle(article);
        });

        // Generate credibility for all loaded articles
        generateAllCredibilityButton.addActionListener(e -> {
            if (generateCredibilityController == null) {
                JOptionPane.showMessageDialog(this, "Credibility feature not available.");
                return;
            }
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No articles loaded.");
                return;
            }
            List<Article> all = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                all.add(listModel.getElementAt(i));
            }
            generateCredibilityController.generateForAll(all);
        });

        // View credibility details for selected article
        viewDetailsButton.addActionListener(e -> {
            if (viewCredibilityDetailsController == null) {
                JOptionPane.showMessageDialog(this, "Credibility details feature not available.");
                return;
            }
            Article article = articleList.getSelectedValue();
            if (article == null) {
                JOptionPane.showMessageDialog(this, "Please select an article first.");
                return;
            }
            if (article.getCredibilityScore() == null) {
                JOptionPane.showMessageDialog(this, "Generate credibility score first.");
                return;
            }
            viewCredibilityDetailsController.showDetails(article);
        });

        // Filter by trust score
        filterCredibilityButton.addActionListener(e -> {
            if (filterCredibilityController == null) {
                JOptionPane.showMessageDialog(this, "Filter feature not available.");
                return;
            }

            var state = viewModel.getState();
            List<Article> currentArticles = state.getArticles();

            if (currentArticles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No articles to filter.");
                return;
            }

            // Store original articles if not already stored
            if (state.getOriginalArticles().isEmpty()) {
                state.setOriginalArticles(new ArrayList<>(currentArticles));
            }

            // Create filter dialog
            showFilterDialog();
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

    public void setFilterNewsController(FilterNewsController filterNewsController) {
        this.filterNewsController = filterNewsController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel,
                                    LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;

        profileButton.addActionListener(e -> {
            if (profileController != null && loginViewModel != null) {
                String username = loginViewModel.getState().getUsername();
                profileController.execute(username);
            }
        });
    }

    public void setCredibilityUseCases(GenerateCredibilityController generateController,
                                       ViewCredibilityDetailsController detailsController,
                                       ViewCredibilityDetailsViewModel detailsViewModel) {
        this.generateCredibilityController = generateController;
        this.viewCredibilityDetailsController = detailsController;
        this.viewCredibilityDetailsViewModel = detailsViewModel;

        this.viewCredibilityDetailsViewModel.addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();
            ViewCredibilityDetailsState state = viewCredibilityDetailsViewModel.getState();

            if ("details".equals(prop)) {
                showCredibilityDetailsDialog(state);
            } else if ("error".equals(prop)) {
                if (state.getError() != null && !state.getError().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            state.getError(),
                            "Credibility Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    public void setFilterCredibilityController(FilterCredibilityController filterCredibilityController) {
        this.filterCredibilityController = filterCredibilityController;
    }

    private void loadArticles() {
        if (controller != null) {
            controller.fetchHeadlines();
        }
        listModel.clear();
        if (viewModel.getState().getArticles() != null) {
            for (Article a : viewModel.getState().getArticles()) {
                listModel.addElement(a);
            }
        }
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
    }

    private void showCredibilityDetailsDialog(ViewCredibilityDetailsState state) {
        double rawSource = state.getSourceScore();
        double rawSentiment = state.getSentimentScore();
        double rawClaim = state.getClaimConfidence();

        double sourceEff = rawSource;
        if (sourceEff < 0.0) sourceEff = 0.0;
        if (sourceEff > 1.0) sourceEff = 1.0;
        double SOURCE_REF = 0.7;
        sourceEff = sourceEff / SOURCE_REF;
        if (sourceEff > 1.0) sourceEff = 1.0;

        double textRaw = 0.5 * rawSentiment + 0.5 * rawClaim;
        if (textRaw < 0.0) textRaw = 0.0;
        if (textRaw > 1.0) textRaw = 1.0;
        double textEff = Math.pow(textRaw, 0.85);
        if (textEff > 1.0) textEff = 1.0;

        double wSource = 0.7;
        double wAI = 0.3;
        double overallEff = wSource * sourceEff + wAI * textEff;

        double overallStored = state.getOverallTrust();
        String level = state.getLevel();

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        if (state.getTitle() != null) {
            sb.append("<b>").append(state.getTitle()).append("</b><br><br>");
        }
        sb.append("<b>Source:</b> ").append(state.getSource()).append("<br><br>");

        sb.append("<b>Subscores</b><br>");
        sb.append("Domain reputation: ")
                .append(String.format("%.3f", sourceEff))
                .append("<br>");

        sb.append("AI analysis for reliability: ")
                .append(String.format("%.3f", textEff))
                .append("<br><br>");

        sb.append("<b>Weights</b><br>");
        sb.append("Domain reputation weight: ")
                .append(String.format("%.2f", wSource))
                .append("<br>");
        sb.append("AI analysis weight: ")
                .append(String.format("%.2f", wAI))
                .append("<br><br>");

        sb.append("<b>Overall trust:</b> ")
                .append(String.format("%.3f", overallStored))
                .append(" (").append(level).append(")")
                .append("<br>");

        sb.append("<b>Rationale</b><br>")
                .append(state.getRationale() == null ? "N/A" : state.getRationale())
                .append("<br>");

        sb.append("</html>");

        JOptionPane.showMessageDialog(
                this,
                new JLabel(sb.toString()),
                "Credibility Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showFilterDialog() {
        var state = viewModel.getState();
        java.util.Set<String> currentFilterLevels = state.getCurrentFilterLevels();

        // Create dialog
        JDialog filterDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Filter by Credibility", true);
        filterDialog.setLayout(new BorderLayout(10, 10));
        filterDialog.setSize(400, 250);
        filterDialog.setLocationRelativeTo(this);

        // Create header panel with label and info button
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel headerLabel = new JLabel("Filter articles based on credibility score:");
        headerLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
        headerLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("ⓘ");
        infoLabel.setFont(new Font("TimesNewRoman", Font.PLAIN, 16));
        infoLabel.setForeground(new Color(100, 100, 200)); // Blue-ish color for info
        infoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        infoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(
                        filterDialog,
                        "High: overallTrust >= 0.8\nMedium: overallTrust >= 0.65\nLow: overallTrust < 0.65",
                        "Trust Score Thresholds",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createHorizontalStrut(3)); // Small gap
        headerPanel.add(infoLabel);

        // Create checkboxes with color icons
        JCheckBox highCheckBox = new JCheckBox("🟢 High Trust");
        JCheckBox mediumCheckBox = new JCheckBox("🟡 Medium Trust");
        JCheckBox lowCheckBox = new JCheckBox("🔴 Low Trust");

        // Pre-select based on current filter state
        highCheckBox.setSelected(currentFilterLevels.contains("High"));
        mediumCheckBox.setSelected(currentFilterLevels.contains("Medium"));
        lowCheckBox.setSelected(currentFilterLevels.contains("Low"));

        // Create checkbox panel
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        checkboxPanel.add(highCheckBox);
        checkboxPanel.add(Box.createVerticalStrut(10));
        checkboxPanel.add(mediumCheckBox);
        checkboxPanel.add(Box.createVerticalStrut(10));
        checkboxPanel.add(lowCheckBox);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton applyButton = new JButton("Apply Filter");
        JButton clearButton = new JButton("Clear Filter");
        buttonPanel.add(applyButton);
        buttonPanel.add(clearButton);

        filterDialog.add(headerPanel, BorderLayout.NORTH);
        filterDialog.add(checkboxPanel, BorderLayout.CENTER);
        filterDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Apply button action
        applyButton.addActionListener(e -> {
            java.util.Set<String> selectedLevels = new java.util.HashSet<>();
            if (highCheckBox.isSelected()) {
                selectedLevels.add("High");
            }
            if (mediumCheckBox.isSelected()) {
                selectedLevels.add("Medium");
            }
            if (lowCheckBox.isSelected()) {
                selectedLevels.add("Low");
            }

            List<Article> currentArticles = state.getArticles();
            if (!currentArticles.isEmpty()) {
                filterCredibilityController.filterArticles(currentArticles, selectedLevels);
            }
            filterDialog.dispose();
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            List<Article> originalArticles = state.getOriginalArticles();
            if (!originalArticles.isEmpty()) {
                state.setArticles(new ArrayList<>(originalArticles));
                state.setCurrentFilterLevels(new java.util.HashSet<>());
                viewModel.firePropertyChange();
            }
            filterDialog.dispose();
        });

        filterDialog.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listModel.clear();

        var state = viewModel.getState();
        List<Article> articles = state.getArticles();
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

    private void openFilter() {
        if (filterNewsController == null) {
            JOptionPane.showMessageDialog(this, "Filtering is not available.");
            return;
        }

        if (filterNewsView == null) {
            java.awt.Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame frame) {
                filterNewsView = new FilterNewsView(frame, filterNewsController);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to open filter dialog.");
                return;
            }
        }

        filterNewsView.setLocationRelativeTo(this);
        filterNewsView.setVisible(true);
    }

    static class ArticleRenderer extends JPanel implements ListCellRenderer<Article> {
        private final JLabel titleLabel = new JLabel();
        private final JLabel sourceLabel = new JLabel();
        private final JLabel trustLabel = new JLabel();

        public ArticleRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.WHITE);

            titleLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 16));
            titleLabel.setForeground(Color.BLACK);

            sourceLabel.setFont(new Font("TimesNewRoman", Font.ITALIC, 13));
            sourceLabel.setForeground(new Color(80, 80, 80));

            trustLabel.setFont(new Font("TimesNewRoman", Font.PLAIN, 13));
            trustLabel.setForeground(new Color(60, 60, 60));

            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
            southPanel.setOpaque(false);
            southPanel.add(sourceLabel);
            southPanel.add(trustLabel);

            add(titleLabel, BorderLayout.CENTER);
            add(southPanel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Article> list,
                Article article,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            titleLabel.setText(article.getTitle());
            sourceLabel.setText("Source: " + article.getSource());

            String level = article.getConfidenceLevel();
            double trust = article.getTrustScore();

            if (article.getCredibilityScore() == null ||
                    level == null ||
                    "Unknown".equalsIgnoreCase(level)) {
                trustLabel.setText("Generate credibility score");
            } else {
                trustLabel.setText(String.format("Trust: %.2f (%s)", trust, level));
            }

            Color baseColor = Color.WHITE;
            if (level != null) {
                if ("High".equalsIgnoreCase(level)) {
                    baseColor = new Color(210, 245, 210); // light green
                } else if ("Medium".equalsIgnoreCase(level)) {
                    baseColor = new Color(255, 250, 205); // light yellow
                } else if ("Low".equalsIgnoreCase(level)) {
                    baseColor = new Color(255, 220, 220); // light red
                }
            }

            if (isSelected) {
                setBackground(baseColor.darker());
            } else {
                setBackground(baseColor);
            }

            return this;
        }
    }
}
