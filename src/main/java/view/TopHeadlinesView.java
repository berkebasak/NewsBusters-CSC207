package view;

import entity.Article;
import interface_adapter.top_headlines.TopHeadlinesController;
import interface_adapter.top_headlines.TopHeadlinesViewModel;
import interface_adapter.search_news.SearchNewsController;


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
    private final TopHeadlinesViewModel viewModel;
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Load Top Headlines");

    private SearchNewsController searchNewsController;
    private final JTextField keywordField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");

    public TopHeadlinesView(TopHeadlinesController controller, TopHeadlinesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel title = new JLabel("Top News Headlines");
        title.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        topBar.add(title);
        topBar.add(refreshButton);

        topBar.add(new JLabel("Keyword:"));
        topBar.add(keywordField);
        topBar.add(searchButton);

        topBar.setBackground(Color.WHITE);
        add(topBar, BorderLayout.NORTH);

        articleList.setCellRenderer(new ArticleRenderer());
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articleList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadArticles());

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
                    if (article != null) openInBrowser(article.getUrl());
                }
            }
        });
    }

    public void setController(TopHeadlinesController controller) {
        this.controller = controller;
    }

    public void setSearchNewsController(SearchNewsController searchNewsController) {
        this.searchNewsController = searchNewsController;
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
                    JOptionPane.INFORMATION_MESSAGE
            );
            state.setError(null);
        }
    }


}
