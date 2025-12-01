package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.discover_page.DiscoverPageController;
import interface_adapter.discover_page.DiscoverPageViewModel;

import interface_adapter.generate_credibility.GenerateCredibilityController;
import interface_adapter.filter_credibility.FilterCredibilityController;
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

public class DiscoverPageView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "discover_page_view";
    private DiscoverPageController controller;
    private final DiscoverPageViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private GenerateCredibilityController generateCredibilityController;
    private ViewCredibilityDetailsController viewCredibilityDetailsController;
    private ViewCredibilityDetailsViewModel viewCredibilityDetailsViewModel;
    private FilterCredibilityController filterCredibilityController;
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Refresh Discover Feed");
    private final JButton backButton = new JButton("Back to Headlines");
    private final JButton generateCredibilityButton = new JButton("Generate Credibility");
    private final JButton generateAllCredibilityButton = new JButton("Generate All Scores");
    private final JButton viewDetailsButton = new JButton("View Details");
    // Filter by trust score
    private final JButton filterButton = new JButton("Filter by Credibility");
    private final JLabel messageLabel = new JLabel();
    private final JPanel messagePanel = new JPanel();
    private final JPanel centerPanel = new JPanel(new CardLayout());

    public DiscoverPageView(DiscoverPageController controller, DiscoverPageViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel title = new JLabel("Discover Page");
        title.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Displaying new articles based on your saved articles");
        subtitle.setFont(new Font("TimesNewRoman", Font.PLAIN, 12));
        subtitle.setForeground(new Color(100, 100, 100));
        topBar.add(title);
        topBar.add(refreshButton);
        topBar.add(backButton);
        topBar.add(generateCredibilityButton);
        topBar.add(generateAllCredibilityButton);
        topBar.add(viewDetailsButton);
        topBar.add(filterButton);
        topBar.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(topBar);
        JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        subtitlePanel.add(subtitle);
        subtitlePanel.setBackground(Color.WHITE);
        topPanel.add(subtitlePanel);
        topPanel.setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);

        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messageLabel.setFont(new Font("TimesNewRoman", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(new Color(100, 100, 100));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        articleList.setCellRenderer(new TopHeadlinesView.ArticleRenderer());
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articleList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        centerPanel.add(scrollPane, "articles");
        centerPanel.add(messagePanel, "message");
        add(centerPanel, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> {
            if (this.controller != null) {
                this.controller.execute();
            }
        });

        backButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(TopHeadlinesView.VIEW_NAME);
            }
        });

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

        generateAllCredibilityButton.addActionListener(e -> {
            if (generateCredibilityController == null) {
                JOptionPane.showMessageDialog(this, "Credibility feature not available.");
                return;
            }
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No articles to score.");
                return;
            }
            List<Article> all = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                all.add(listModel.getElementAt(i));
            }
            generateCredibilityController.generateForAll(all);
        });

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
                JOptionPane.showMessageDialog(this, "Generate a credibility score for this article first.");
                return;
            }
            viewCredibilityDetailsController.showDetails(article);
        });

        // Filter by trust score
        filterButton.addActionListener(e -> {
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

        articleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Article article = articleList.getSelectedValue();
                    if (article != null) {
                        openInBrowser(article.getUrl());
                    }
                }
            }
        });

        updateView();
    }

    public void setController(DiscoverPageController controller) {
        this.controller = controller;
    }

    /**
     * Loads articles when the view becomes visible.
     * This ensures articles are loaded after the user has logged in.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && controller != null) {
            // Load articles when view becomes visible (after login)
            controller.execute();
        }
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setCredibilityUseCases(GenerateCredibilityController generateController,
                                       ViewCredibilityDetailsController detailsController,
                                       ViewCredibilityDetailsViewModel detailsViewModel) {
        this.generateCredibilityController = generateController;
        this.viewCredibilityDetailsController = detailsController;
        this.viewCredibilityDetailsViewModel = detailsViewModel;
    }

    public void setFilterCredibilityController(FilterCredibilityController filterCredibilityController) {
        this.filterCredibilityController = filterCredibilityController;
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
    }

    private void showFilterDialog() {
        var state = viewModel.getState();
        java.util.Set<String> currentFilterLevels = state.getCurrentFilterLevels();

        // Create dialog
        JDialog filterDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Filter by Credibility", true);
        filterDialog.setLayout(new BorderLayout(10, 10));
        filterDialog.setSize(400, 250);
        filterDialog.setLocationRelativeTo(this);

        // Create header label
        JLabel headerLabel = new JLabel("Filter articles based on credibility score:");
        headerLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

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

        filterDialog.add(headerLabel, BorderLayout.NORTH);
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

    private void updateView() {
        var state = viewModel.getState();
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();

        if (state.getHasNoHistory() || state.getHasNoArticles()) {
            messageLabel.setText(state.getMessage());
            cardLayout.show(centerPanel, "message");
            listModel.clear();
        } else {
            listModel.clear();
            java.util.List<Article> articles = state.getArticles();
            if (articles != null && !articles.isEmpty()) {
                for (Article a : articles) {
                    listModel.addElement(a);
                }
                cardLayout.show(centerPanel, "articles");
            } else {
                messageLabel.setText("No articles to display.");
                cardLayout.show(centerPanel, "message");
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int selectedIndex = articleList.getSelectedIndex();

        updateView();

        // Show error message in popup if there's a filter error (message set but articles should remain visible)
        var state = viewModel.getState();
        String message = state.getMessage();
        // If message exists but hasNoHistory and hasNoArticles are both false, it's a filter error
        // Show popup and keep articles visible (like TopHeadlinesView does)
        if (message != null && !message.isEmpty() && !state.getHasNoHistory() && !state.getHasNoArticles()) {
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Filter Error",
                    JOptionPane.INFORMATION_MESSAGE);
            state.setMessage(null); // Clear the message after showing
        }

        if (selectedIndex >= 0 && selectedIndex < listModel.getSize()) {
            articleList.setSelectedIndex(selectedIndex);
            articleList.ensureIndexIsVisible(selectedIndex);
        }
    }

    static class DiscoverArticleRenderer extends JPanel implements ListCellRenderer<Article> {
        private final JLabel titleLabel = new JLabel();
        private final JLabel sourceLabel = new JLabel();
        private final JLabel trustLabel = new JLabel();

        public DiscoverArticleRenderer() {
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
                    baseColor = new Color(210, 245, 210);
                } else if ("Medium".equalsIgnoreCase(level)) {
                    baseColor = new Color(255, 250, 205);
                } else if ("Low".equalsIgnoreCase(level)) {
                    baseColor = new Color(255, 220, 220);
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
