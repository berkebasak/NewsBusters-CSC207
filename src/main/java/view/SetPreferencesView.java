package view;

import entity.UserPreferences;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.set_preferences.SetPreferencesController;
import interface_adapter.set_preferences.SetPreferencesState;
import interface_adapter.set_preferences.SetPreferencesViewModel;
import utility.CountryCodeConverter;
import utility.LanguageCodeConverter;
import utility.NewsTopicExtractor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SetPreferencesView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = SetPreferencesViewModel.VIEW_NAME;

    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;

    // Utility classes for data retrieval
    NewsTopicExtractor newsTopicExtractor = new NewsTopicExtractor("news-topics.txt");
    CountryCodeConverter countryConverter = new CountryCodeConverter("country-codes.txt");
    LanguageCodeConverter languageConverter = new LanguageCodeConverter("language-codes.txt");

    // UI Components
    private final ArrayList<JCheckBox> preferredTopicCheckBoxes = new ArrayList<>();
    private final DefaultListModel<String> blockedSourcesListModel = new DefaultListModel<>();
    private final JList<String> blockedSourcesList = new JList<>(blockedSourcesListModel);
    private final JTextField blockedSourceInputField = new JTextField(20);
    private final JButton addSourceButton = new JButton("Add Source");
    private final JButton removeSourceButton = new JButton("Remove Selected");
    private final JComboBox<String> languageComboBox = new JComboBox<>();
    private final JComboBox<String> countryComboBox = new JComboBox<>();
    private final JButton saveButton = new JButton("Save Preferences");
    private final JButton closeButton = new JButton("Close");
    private final JLabel messageLabel = new JLabel();

    public SetPreferencesView(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        this.setPreferencesViewModel.addPropertyChangeListener(this);

        // --- Main Layout Setup: Use GridBagLayout for better control ---
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setSize(400,300);
        GridBagConstraints gbc = new GridBagConstraints();

        // Title and Message Label (Row 0)
        JLabel titleLabel = new JLabel("Configure News Preferences");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 15, 0); // Padding at the bottom
        add(titleLabel, gbc);

        // Message Label (Row 1) - To display errors or success messages
        messageLabel.setText("Configure your preferences and click Save.");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(messageLabel, gbc);

        // Preference Panels (Rows 2 & 3)
        // Topics and Sources on the left (Column 0), Language/Country on the right (Column 1)

        // Preferred News Topics Panel (Column 0, Row 2)
        JPanel preferredTopicsPanel = initPreferredNewsTopics();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.6; // Give more width to this panel
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 0, 10, 20); // Top, Left, Bottom, Right padding
        add(preferredTopicsPanel, gbc);

        // Language and Country Panel (Column 1, Row 2)
        JPanel languageCountryPanel = initLanguageAndCountry();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.4; // Less width
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 20, 10, 0);
        add(languageCountryPanel, gbc);

        // Blocked Sources Panel (Column 0, Row 3)
        JPanel blockedSourcesPanel = initBlockedSources();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across both columns for the list, but list panel is only in col 0
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow this panel to take up extra vertical space
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 20, 0);
        add(blockedSourcesPanel, gbc);


        // Button Panel (Row 4)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(buttonPanel, gbc);

        // --- Attach Action Listeners ---
        addSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String source = blockedSourceInputField.getText().trim();
                if (!source.isEmpty() && !blockedSourcesListModel.contains(source)) {
                    blockedSourcesListModel.addElement(source);
                    blockedSourceInputField.setText("");
                } else if (!source.isEmpty() && blockedSourcesListModel.contains(source)) {
                    JOptionPane.showMessageDialog(blockedSourcesPanel.getParent(), "Source already blocked.");
                }
            }
        });

        removeSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = blockedSourcesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    blockedSourcesListModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(blockedSourcesPanel.getParent(), "No sources selected.");
                }
            }
        });

        saveButton.addActionListener(e -> {
            if (setPreferencesController != null && loginViewModel != null) {
                LoginState state = loginViewModel.getState();
                ArrayList<String> preferredTopics = new ArrayList<>();
                for (JCheckBox checkBox : preferredTopicCheckBoxes) {
                    if (checkBox.isSelected()) {
                        preferredTopics.add(checkBox.getText());
                    }
                }
                ArrayList<String> blockedSources = new ArrayList<>();
                for (int i = 0; i < blockedSourcesListModel.getSize(); i++) {
                    blockedSources.add(blockedSourcesListModel.getElementAt(i));
                }
                Object lo = languageComboBox.getSelectedItem();
                Object co = countryComboBox.getSelectedItem();
                String language = lo == null ? null : lo.toString();
                String country = co == null ? null : co.toString();
                setPreferencesController.save(state.getUsername(), new UserPreferences(
                        preferredTopics,
                        blockedSources,
                        languageConverter.getLanguageCode(language),
                        countryConverter.getCountryCode(country)
                ));
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Configuration error: Controller or Login State missing.");
            }
        });

        closeButton.addActionListener(e -> {
            if (viewManagerModel != null) {
                viewManagerModel.changeView(ProfileViewModel.VIEW_NAME);
            }
        });
    }

    private JPanel initPreferredNewsTopics() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Preferred News Topics"));

        JPanel newsTopicCheckBoxesPanel = new JPanel();
        // Use a wrapping layout or a grid that is not fixed to 4x4
        // A GridLayout with many columns (e.g., 8) will make it compact
        newsTopicCheckBoxesPanel.setLayout(new GridLayout(0, 4, 5, 5)); // 0 rows means dynamic number of rows

        for (String topic : newsTopicExtractor.getAllTopicNames()) {
            JCheckBox checkBox = new JCheckBox(topic);
            preferredTopicCheckBoxes.add(checkBox);
            newsTopicCheckBoxesPanel.add(checkBox);
        }

        panel.add(new JScrollPane(newsTopicCheckBoxesPanel), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(300, 150)); // Set a reasonable size hint
        return panel;
    }

    private JPanel initBlockedSources() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Blocked Sources"));

        // Source List
        blockedSourcesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(blockedSourcesList);
        listScrollPane.setPreferredSize(new Dimension(300, 100));
        panel.add(listScrollPane, BorderLayout.CENTER);

        // Input and Control Buttons Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.add(blockedSourceInputField);
        controlPanel.add(addSourceButton);
        controlPanel.add(removeSourceButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel initLanguageAndCountry() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Region & Language"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Populate and add Language
        JLabel languageLabel = new JLabel("Language:");
        for (String lang : languageConverter.getAllLanguageNames())
            languageComboBox.addItem(lang);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(languageLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(languageComboBox, gbc);

        // Populate and add Country
        JLabel countryLabel = new JLabel("Country:");
        for (String country : countryConverter.getAllCountryNames())
            countryComboBox.addItem(country);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(countryLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(countryComboBox, gbc);

        return panel;
    }

    public void setSetPreferencesController(SetPreferencesController setPreferencesController) {
        this.setPreferencesController = setPreferencesController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

//    public void setSaveArticleUseCase(SaveArticleController controller,
//                                      SaveArticleViewModel viewModel) {
//        this.saveController = controller;
//        this.saveViewModel = viewModel;
//
//        this.saveViewModel.addPropertyChangeListener(evt -> {
//            if ("message".equals(evt.getPropertyName())) {
//                String msg = (String) evt.getNewValue();
//                if (msg != null && !msg.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, msg);
//                }
//            }
//        });
//    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SetPreferencesState state = setPreferencesViewModel.getState();

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError());
            state.setError(null); // Acknowledge error
        } else if (state.getMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getMessage());
            state.setMessage(null); // Acknowledge message
        } else {
            // State update - Populate UI fields
            UserPreferences userPreferences = state.getUserPreferences();
            ArrayList<String> preferredTopics = userPreferences.getPreferredTopics();
            ArrayList<String> blockedSources = userPreferences.getBlockedSources();
            String language = languageConverter.getLanguageName(userPreferences.getLanguage());
            String country = countryConverter.getCountryName(userPreferences.getRegion());

            // Update Checkboxes
            for (JCheckBox checkBox : preferredTopicCheckBoxes) {
                checkBox.setSelected(preferredTopics.contains(checkBox.getText()));
            }

            // Update Blocked Sources List
            blockedSourcesListModel.clear();
            for (String source : blockedSources)
                blockedSourcesListModel.addElement(source);

            // Update ComboBoxes
            if (language != null) languageComboBox.setSelectedItem(language);
            if (country != null) countryComboBox.setSelectedItem(country);

            messageLabel.setForeground(Color.BLACK);
            messageLabel.setText("Preferences loaded successfully.");
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
