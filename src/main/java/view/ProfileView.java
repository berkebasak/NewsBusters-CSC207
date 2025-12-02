package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.set_preferences.SetPreferencesViewModel;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import interface_adapter.load_saved_articles.LoadSavedArticlesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

public class ProfileView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = "profile";

    private final ProfileViewModel profileViewModel;
    private ProfileController profileController;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;
    private LoadSavedArticlesController loadSavedArticlesController;

    private final JLabel usernameLabel = new JLabel();
    private final DefaultListModel<Article> historyListModel = new DefaultListModel<>();
    private final JList<Article> historyList = new JList<>(historyListModel);
    private final JButton backButton = new JButton("Back");
    private final JButton signOutButton = new JButton("Sign Out");

    public ProfileView(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        this.profileViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBackground(new Color(240, 240, 240));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JPanel sidebarTop = new JPanel();
        sidebarTop.setLayout(new BoxLayout(sidebarTop, BoxLayout.Y_AXIS));
        sidebarTop.setBackground(new Color(240, 240, 240));
        sidebarTop.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JButton savedArticlesButton = createSidebarButton("Saved Articles");
        JButton preferencesButton = createSidebarButton("Preferences");
        JButton accountSettingsButton = createSidebarButton("Account Settings");

        sidebarTop.add(savedArticlesButton);
        sidebarTop.add(Box.createVerticalStrut(10));
        sidebarTop.add(preferencesButton);
        sidebarTop.add(Box.createVerticalStrut(10));
        sidebarTop.add(accountSettingsButton);

        JPanel sidebarBottom = new JPanel(new BorderLayout());
        sidebarBottom.setBackground(new Color(240, 240, 240));
        sidebarBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Style Sign Out button
        signOutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarBottom.add(signOutButton, BorderLayout.SOUTH);

        sidebarPanel.add(sidebarTop, BorderLayout.NORTH);
        sidebarPanel.add(sidebarBottom, BorderLayout.SOUTH);

        add(sidebarPanel, BorderLayout.WEST);

        // Main Content
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);

        // Header in Main Content
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("User Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(title, BorderLayout.WEST);

        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        headerPanel.add(usernameLabel, BorderLayout.EAST);

        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // History List
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20),
                BorderFactory.createTitledBorder("Recently Opened Articles")));

        historyList.setCellRenderer(new ArticleRenderer());
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Back Button (Floating or Top Right? Let's put it in header for now or keep it
        // separate)
        // The user didn't explicitly ask to remove it, but sidebar usually implies
        // navigation.
        // Let's keep "Back" in the header for easy return to headlines.
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setBackground(Color.WHITE);
        headerRightPanel.add(usernameLabel);
        headerRightPanel.add(backButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        mainContentPanel.add(historyPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // Listeners
        backButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(TopHeadlinesViewModel.VIEW_NAME);
                viewManagerModel.firePropertyChange();
            }
        });

        signOutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to sign out?",
                    "Sign Out",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Clear state if needed
                if (loginViewModel != null) {
                    loginViewModel.firePropertyChange("reset");
                }
                if (viewManagerModel != null) {
                    viewManagerModel.changeView(LoginViewModel.VIEW_NAME);
                    viewManagerModel.firePropertyChange();
                }
            }
        });

        // Placeholder listeners for new buttons
        savedArticlesButton.addActionListener(e -> {
                    if (loadSavedArticlesController == null || loginViewModel == null) {
                        JOptionPane.showMessageDialog(this, "Saved Articles not available.");
                        return;
                    }
                    String username = loginViewModel.getState().getUsername();
                    loadSavedArticlesController.execute(username);
                    //the presenter will switch to LoadSavedArticlesView
                });
        preferencesButton.addActionListener(e -> {
            viewManagerModel.changeView(SetPreferencesViewModel.VIEW_NAME);
        });
        accountSettingsButton
                .addActionListener(e -> JOptionPane.showMessageDialog(this, "Account Settings feature coming soon!"));

        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Article article = historyList.getSelectedValue();
                    if (article != null) {
                        openInBrowser(article.getUrl());
                    }
                }
            }
        });
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    public void setLoadSavedArticlesController(LoadSavedArticlesController loadSavedArticlesController) {
        this.loadSavedArticlesController = loadSavedArticlesController;
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
        ProfileState state = profileViewModel.getState();
        usernameLabel.setText("User: " + state.getUsername());

        historyListModel.clear();
        for (Article article : state.getHistory()) {
            historyListModel.addElement(article);
        }

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError());
            state.setError(null);
        }
    }

    // Reusing ArticleRenderer logic or creating a simple one
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
        public Component getListCellRendererComponent(JList<? extends Article> list, Article value, int index,
                boolean isSelected, boolean cellHasFocus) {
            titleLabel.setText(value.getTitle());

            String timestamp = "";
            if (value.getAccessedAt() != null) {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm");
                timestamp = " - Accessed: " + value.getAccessedAt().format(formatter);
            }
            sourceLabel.setText(value.getSource() + timestamp);

            if (isSelected) {
                setBackground(new Color(225, 235, 255));
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
