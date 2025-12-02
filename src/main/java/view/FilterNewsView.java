package view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import interface_adapter.filter_news.FilterNewsController;

import javax.swing.*;
import java.awt.*;

/**
 * Filter News popup window. Lets the user choose one or more news categories. They can cancel, clear or apply filter.
 */
public class FilterNewsView extends JDialog {

    private final FilterNewsController filterNewsController;

    private final Map<JCheckBox, String> checkboxMap = new LinkedHashMap<>();

    private final JButton applyButton = new JButton("Apply Filters");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton clearButton = new JButton("Clear Filters");

    /**
     * Creates the Filter News popup.
     * @param frame the main JFrame
     * @param filterNewsController the controller for applying filters
     */
    public FilterNewsView(JFrame frame, FilterNewsController filterNewsController) {
        super(frame, "Filter News by Topic", true);
        this.filterNewsController = filterNewsController;

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // List of all NewsData.io categories
        final List<String> topics = List.of(
                "business", "entertainment", "environment", "food", "health",
                "politics", "science", "sports", "technology", "world",
                "lifestyle", "education", "crime", "tourism", "automobile"
        );

        final JPanel topicsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        topicsPanel.setBackground(Color.WHITE);

        for (String topic : topics) {
            final String label = capitalize(topic);
            final JCheckBox box = new JCheckBox(label);
            box.setBackground(Color.WHITE);

            checkboxMap.put(box, topic);
            topicsPanel.add(box);
        }

        final JScrollPane scrollPane = new JScrollPane(topicsPanel);
        scrollPane.setPreferredSize(new Dimension(350, 240));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(clearButton);

        final JLabel header = new JLabel("Select one or more topics:");
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(frame);

        applyButton.addActionListener(e -> applyFilters());
        cancelButton.addActionListener(e -> setVisible(false));
        clearButton.addActionListener(e -> clearFilters());
    }

    /**
     * Applies the selected topic filters to the news articles.
     */
    private void applyFilters() {
        final List<String> selected = new ArrayList<>();

        for (var entry : checkboxMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                selected.add(entry.getValue());
            }
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one topic.");
            return;
        }

        filterNewsController.execute(selected);
        setVisible(false);
    }

    /**
     * Collects selected topics and runs the filter use case.
     */
    private void clearFilters() {
        for (JCheckBox box : checkboxMap.keySet()) {
            box.setSelected(false);
        }
        filterNewsController.clearFilter();

        setVisible(false);
    }

    /**
     * Capitalizes the first letter of a string.
     * @param s the string to capitalize, may be null or empty
     * @return the capitalized string, or the original string if it is null or empty
     */
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
