package view;

import entity.Article;
import interface_adapter.ViewManagerModel;
import interface_adapter.discover_page.DiscoverPageController;
import interface_adapter.discover_page.DiscoverPageViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

public class DiscoverPageView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "discover_page_view";
    private DiscoverPageController controller;
    private final DiscoverPageViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Refresh Discover Feed");
    private final JButton backButton = new JButton("Back to Headlines");
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
        JLabel subtitle = new JLabel("Articles based on your saved articles");
        subtitle.setFont(new Font("TimesNewRoman", Font.PLAIN, 12));
        subtitle.setForeground(new Color(100, 100, 100));
        topBar.add(title);
        topBar.add(refreshButton);
        topBar.add(backButton);
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
                viewManagerModel.setState(TopHeadlinesView.VIEW_NAME);
                viewManagerModel.firePropertyChange();
            }
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
        if (controller != null) {
            controller.execute();
        }
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open link: " + e.getMessage());
        }
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
        updateView();
    }
}

