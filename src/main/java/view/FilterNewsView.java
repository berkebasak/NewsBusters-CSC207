package view;

import interface_adapter.filter_news.FilterNewsController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * View for the Filter News use case.
 * Lets the user select one or more topics and apply filters.
 */
public class FilterNewsView extends JDialog {

    private final FilterNewsController filterNewsController;

    private final JCheckBox businessCheckBox = new JCheckBox("Business");
    private final JCheckBox sportsCheckBox = new JCheckBox("Sports");
    private final JCheckBox technologyCheckBox = new JCheckBox("Technology");
    private final JCheckBox healthCheckBox = new JCheckBox("Health");
    private final JCheckBox entertainmentCheckBox = new JCheckBox("Entertainment");
    private final JCheckBox scienceCheckBox = new JCheckBox("Science");

    private final JButton applyButton = new JButton("Apply Filters");
    private final JButton cancelButton = new JButton("Cancel");

    /**
     * Creates a new FilterNewsView dialog.
     *
     * @param frame     main window frame
     * @param filterNewsController the Filter News controller
     */
    public FilterNewsView(JFrame frame, FilterNewsController filterNewsController) {
        super(frame, "Filter News by Topic", true);
        this.filterNewsController = filterNewsController;

        setLayout(new BorderLayout(10, 10));
        JPanel topicsPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        topicsPanel.add(businessCheckBox);
        topicsPanel.add(sportsCheckBox);
        topicsPanel.add(technologyCheckBox);
        topicsPanel.add(healthCheckBox);
        topicsPanel.add(entertainmentCheckBox);
        topicsPanel.add(scienceCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        add(new JLabel("Select one or more topics:"), BorderLayout.NORTH);
        add(topicsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(frame);

        applyButton.addActionListener(e -> onApply());
        cancelButton.addActionListener(e -> setVisible(false));
    }

    /**
     * Collects selected topics and calls the use case.
     */
    private void onApply() {
        java.util.List<String> selectedTopics = new ArrayList<>();

        if (businessCheckBox.isSelected()) {
            selectedTopics.add("business");
        }

        if (sportsCheckBox.isSelected()) {
            selectedTopics.add("sports");
        }

        if (technologyCheckBox.isSelected()) {
            selectedTopics.add("technology");
        }

        if (healthCheckBox.isSelected()) {
            selectedTopics.add("health");
        }

        if (entertainmentCheckBox.isSelected()) {
            selectedTopics.add("entertainment");
        }

        if (scienceCheckBox.isSelected()) {
            selectedTopics.add("science");
        }

        if (selectedTopics.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one topic.");
            return;
        }

        filterNewsController.execute(selectedTopics);
        setVisible(false);
    }
}
