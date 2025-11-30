package view;

import entity.UserPreferences;
import interface_adapter.ViewManagerModel;
import interface_adapter.set_preferences.SetPreferencesController;
import interface_adapter.set_preferences.SetPreferencesState;
import interface_adapter.set_preferences.SetPreferencesViewModel;
import utility.CountryCodeExtractor;
import utility.LanguageCodeExtractor;
import utility.NewsTopicExtractor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SetPreferencesView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = SetPreferencesViewModel.VIEW_NAME;

    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController;
    private ViewManagerModel viewManagerModel;

    CountryCodeExtractor countryExtractor = new CountryCodeExtractor("country-codes.txt");
    LanguageCodeExtractor languageExtractor = new LanguageCodeExtractor("language-codes.txt");
    NewsTopicExtractor newsTopicExtractor = new NewsTopicExtractor("news-topics.txt");

    private final ArrayList<JCheckBox> preferredTopicCheckBoxes = new ArrayList<>();
    private final DefaultListModel<String> blockedSourcesListModel = new DefaultListModel<>();
    private final JList<String> blockedSourcesList = new JList<>();
    private final JComboBox<String> languageComboBox = new JComboBox<>();
    private final JComboBox<String> countryComboBox = new JComboBox<>();
    private final JButton saveButton = new JButton("Save");
    private final JLabel messageLabel = new JLabel();

    private void initPreferredNewsTopics() {
        JPanel preferredNewsTopicsPanel = new JPanel();
        preferredNewsTopicsPanel.setLayout(new BoxLayout(preferredNewsTopicsPanel, BoxLayout.X_AXIS));

        JLabel preferredNewsTopicsLabel = new JLabel("Preferred News Topics: ");
        preferredNewsTopicsPanel.add(preferredNewsTopicsLabel);

        JPanel newsTopicCheckBoxesPanel = new JPanel();
        newsTopicCheckBoxesPanel.setLayout(new GridLayout(4, 4));
        for (String topic :  newsTopicExtractor.getAllTopicNames()) {
            JCheckBox checkBox = new JCheckBox(topic);
            preferredTopicCheckBoxes.add(checkBox);
            newsTopicCheckBoxesPanel.add(checkBox);
        }
        preferredNewsTopicsPanel.add(newsTopicCheckBoxesPanel);

        add(preferredNewsTopicsPanel);
    }

    private void initBlockedSources() {
        JPanel blockedSourcesPanel = new JPanel();
        blockedSourcesPanel.setLayout(new BoxLayout(blockedSourcesPanel, BoxLayout.X_AXIS));

        JLabel blockedSourcesLabel = new JLabel("Blocked Sources: ");

        SetPreferencesState state = setPreferencesViewModel.getState();
        UserPreferences preferences = state.getUserPreferences();
        ArrayList<String> blockedSources = preferences == null ? new ArrayList<>() : preferences.getBlockedSources();
        if (blockedSources.isEmpty()) {
            blockedSourcesLabel.setText("Blocked Sources: None");
        }
        blockedSourcesPanel.add(blockedSourcesLabel);

        for (String source : blockedSources)
            blockedSourcesListModel.addElement(source);
        blockedSourcesList.setModel(blockedSourcesListModel);

        blockedSourcesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        blockedSourcesPanel.add(blockedSourcesList);

        add(blockedSourcesPanel);
    }

    private void initLanguages() {
        JPanel languagePanel = new JPanel();
        languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.X_AXIS));

        JLabel languageLabel = new JLabel("Language: ");
        languagePanel.add(languageLabel);

        for (String lang : languageExtractor.getAllLanguageNames())
            languageComboBox.addItem(lang);
        languagePanel.add(languageComboBox);

        add(languagePanel);
    }

    private void initCountries() {
        JPanel countryPanel = new JPanel();
        countryPanel.setLayout(new BoxLayout(countryPanel, BoxLayout.X_AXIS));

        JLabel countryLabel = new JLabel("Country: ");
        countryPanel.add(countryLabel);

        for (String country : countryExtractor.getAllCountryNames())
            countryComboBox.addItem(country);
        countryPanel.add(countryComboBox);

        add(countryPanel);
    }

    public SetPreferencesView(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        this.setPreferencesViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initPreferredNewsTopics();

        initBlockedSources();

        initLanguages();

        initCountries();

        add(saveButton);

        saveButton.addActionListener(e -> {
           if (setPreferencesController != null) {
               SetPreferencesState state = setPreferencesViewModel.getState();
               ArrayList<String> preferredTopics = new ArrayList<>();
               for (JCheckBox checkBox : preferredTopicCheckBoxes) {
                   if (checkBox.isSelected()) {
                       preferredTopics.add(checkBox.getText());
                   }
               }
               ArrayList<String> blockedSources = new ArrayList<>();
               for (int i = 0; i <  blockedSourcesListModel.getSize(); i++) {
                   blockedSources.add(blockedSourcesListModel.getElementAt(i));
               }
               Object lo = languageComboBox.getSelectedItem();
               Object co = countryComboBox.getSelectedItem();
               String language = lo == null ? null : lo.toString();
               String country = co == null ? null : co.toString();
               setPreferencesController.save(state.getUsername(), new UserPreferences(
                       preferredTopics, blockedSources, language, country
               ));
           }
        });
    }

    public void setSetPreferencesController(SetPreferencesController setPreferencesController) {
        this.setPreferencesController = setPreferencesController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        SetPreferencesState state = setPreferencesViewModel.getState();

        if (state.getError() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getError());
        } else if (state.getMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getMessage());
        } else {
        }
    }

    public static void main(String[] args) {
        SetPreferencesView view = new SetPreferencesView(new SetPreferencesViewModel());
        JFrame test =  new JFrame();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.add(view);
        test.pack();
        test.setVisible(true);
    }
}
