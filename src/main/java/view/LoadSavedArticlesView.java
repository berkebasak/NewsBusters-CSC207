package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.load_saved_articles.LoadSavedArticlesState;
import interface_adapter.load_saved_articles.LoadSavedArticlesViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileViewModel;

public class LoadSavedArticlesView extends JPanel implements PropertyChangeListener {

    private static final int BOARDER_VALUE_1 = 20;
    private static final int BOARDER_VALUE_2 = 5;
    private static final int FONT_VALUE_1 = 28;
    private static final int FONT_VALUE_2 = 16;
    private static final int FONT_VALUE_3 = 14;
    private static final int FONT_VALUE_4 = 12;
    private static final int COLOR_R = 225;
    private static final int COLOR_G = 235;
    private static final int COLOR_B = 255;

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
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(BOARDER_VALUE_1, BOARDER_VALUE_1,
                BOARDER_VALUE_1, BOARDER_VALUE_1));

        final JLabel title = new JLabel("Saved Articles");
        title.setFont(new Font("Segoe UI", Font.BOLD, FONT_VALUE_1));
        headerPanel.add(title, BorderLayout.WEST);

        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, FONT_VALUE_2));

        final JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(Color.WHITE);
        headerRight.add(usernameLabel);
        headerRight.add(backButton);
        headerPanel.add(headerRight, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // List + ScrollPane
        final JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(0, BOARDER_VALUE_1, BOARDER_VALUE_1, BOARDER_VALUE_1),
                        BorderFactory.createTitledBorder("Your Saved Articles")
                )
        );

        savedList.setCellRenderer(new ArticleRenderer());
        savedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final JScrollPane scrollPane = new JScrollPane(savedList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        add(listPanel, BorderLayout.CENTER);

        // Listeners
        backButton.addActionListener(event -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(ProfileViewModel.VIEW_NAME);
                viewManagerModel.firePropertyChange();
            }
        });

        savedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // double click
                    final Article article = savedList.getSelectedValue();
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
        }
        else {
            try {
                Desktop.getDesktop().browse(new URI(url));
            }
            catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(this, "Cannot open link: " + ex.getMessage());
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoadSavedArticlesState state = viewModel.getState();
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

        ArticleRenderer() {
            setLayout(new BorderLayout(BOARDER_VALUE_2, BOARDER_VALUE_2));
            setBorder(BorderFactory.createEmptyBorder(BOARDER_VALUE_2, BOARDER_VALUE_2,
                    BOARDER_VALUE_2, BOARDER_VALUE_2));
            setBackground(Color.WHITE);

            titleLabel.setFont(new Font("TimesNewRoman", Font.BOLD, FONT_VALUE_3));
            sourceLabel.setFont(new Font("TimesNewRoman", Font.ITALIC, FONT_VALUE_4));
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
            final String src;
            if (value.getSource() == null) {
                src = "";
            }
            else {
                src = value.getSource();
            }

            sourceLabel.setText(src);

            if (isSelected) {
                setBackground(new Color(COLOR_R, COLOR_G, COLOR_B));
            }
            else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
