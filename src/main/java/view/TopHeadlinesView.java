package view;

import entity.Article;
import interface_adapter.top_headlines.TopHeadlinesController;
import interface_adapter.top_headlines.TopHeadlinesViewModel;

import interface_adapter.save_article.SaveArticleController;
import interface_adapter.save_article.SaveArticleViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class TopHeadlinesView extends JPanel {

    public static final String VIEW_NAME = "top_headlines_view";
    private TopHeadlinesController controller;
    private final TopHeadlinesViewModel viewModel;
    private SaveArticleController saveController;
    private SaveArticleViewModel saveViewModel;
    private final DefaultListModel<Article> listModel = new DefaultListModel<>();
    private final JList<Article> articleList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Load Top Headlines");
    private final JButton saveButton = new JButton("Save Article");

    public TopHeadlinesView(TopHeadlinesController controller, TopHeadlinesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.saveController = null;
        this.saveViewModel = null;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel title = new JLabel("Top News Headlines");
        title.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        topBar.add(title);
        topBar.add(refreshButton);
        topBar.add(saveButton);
        topBar.setBackground(Color.WHITE);
        add(topBar, BorderLayout.NORTH);

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
}