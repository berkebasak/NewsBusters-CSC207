package view;

import interface_adapter.search_news.SearchNewsController;
import interface_adapter.search_news.SearchNewsState;
import interface_adapter.search_news.SearchNewsViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Search News use case (User Story 9).
 */
public class SearchNewsView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "search news";
    private final SearchNewsViewModel searchNewsViewModel;

    private final JTextField keywordInputField = new JTextField(15);
    private final JLabel errorLabel = new JLabel();

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> resultsList = new JList<>(listModel);

    private final JButton searchButton;
    private final JButton cancelButton;
    private SearchNewsController searchNewsController;

    public SearchNewsView(SearchNewsViewModel searchNewsViewModel) {
        this.searchNewsViewModel = searchNewsViewModel;
        this.searchNewsViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(SearchNewsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel keywordPanel = new LabelTextPanel(
                new JLabel(SearchNewsViewModel.KEYWORD_LABEL),
                keywordInputField
        );

        // Buttons
        final JPanel buttons = new JPanel();
        searchButton = new JButton(SearchNewsViewModel.SEARCH_BUTTON_LABEL);
        cancelButton = new JButton(SearchNewsViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(searchButton);
        buttons.add(cancelButton);

        // Button listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(searchButton) && searchNewsController != null) {
                    final SearchNewsState currentState = searchNewsViewModel.getState();
                    searchNewsController.excute(currentState.getKeyword());
                }
            }
        });

        cancelButton.addActionListener(this);

        addKeywordListener();

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(keywordPanel);
        this.add(errorLabel);

        // Results list with scroll
        final JScrollPane scrollPane = new JScrollPane(resultsList);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane);

        this.add(buttons);
    }

    private void addKeywordListener() {
        keywordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void updateState() {
                final SearchNewsState currentState = searchNewsViewModel.getState();
                currentState.setKeyword(keywordInputField.getText());
                searchNewsViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateState();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Cancel not implemented yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Presenter fires propertyChange with the new SearchNewsState as the value
        final SearchNewsState state = (SearchNewsState) evt.getNewValue();

        // Update input and error label
        keywordInputField.setText(state.getKeyword());
        errorLabel.setText(state.getError() == null ? "" : state.getError());

        // Update results list
        listModel.clear();
        if (state.getArticles() != null) {
            state.getArticles().forEach(article ->
                    listModel.addElement(article.getTitle() + " (" + article.getSource() + ")"));
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSearchNewsController(SearchNewsController controller) {
        this.searchNewsController = controller;
    }
}
