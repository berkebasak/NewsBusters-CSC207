package view;

import interface_adapter.filter_news.FilterNewsController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter News popup window.
 * Lets the user choose one or more news categories.
 * Clean, dynamic, and easy to maintain.
 */
public class FilterNewsView extends JDialog {

    private final FilterNewsController filterNewsController;

    private final Map<JCheckBox, String> checkboxMap = new LinkedHashMap<>();

    private final JButton applyButton = new JButton("Apply Filters");
    private final JButton cancelButton = new JButton("Cancel");

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

        // ===== List of all NewsData.io categories you support =====
        List<String> topics = List.of(
                "business", "entertainment", "environment", "food", "health",
                "politics", "science", "sports", "technology", "world",
                "lifestyle", "education", "crime", "tourism", "automobile"
        );

        JPanel topicsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        topicsPanel.setBackground(Color.WHITE);

        // Create checkboxes
        for (String topic : topics) {
            String label = capitalize(topic);
            JCheckBox box = new JCheckBox(label);
            box.setBackground(Color.WHITE);

            checkboxMap.put(box, topic);
            topicsPanel.add(box);
        }

        JScrollPane scrollPane = new JScrollPane(topicsPanel);
        scrollPane.setPreferredSize(new Dimension(350, 240));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        JLabel header = new JLabel("Select one or more topics:");
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(frame);

        applyButton.addActionListener(e -> applyFilters());
        cancelButton.addActionListener(e -> setVisible(false));
    }

     // Collects selected topics and runs the filter use case.
    private void applyFilters() {
        List<String> selected = new ArrayList<>();

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

    //Capitalize first letter.
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
