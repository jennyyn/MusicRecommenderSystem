package com.jennyyn.recommender.view;


import com.jennyyn.recommender.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class WritingPanel extends JPanel {

    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JComboBox<String> modeDropdown;

    private final MainController controller;

    public WritingPanel(MainController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        // ---- Input area ----
        inputArea = new JTextArea(8, 40);
        inputArea.setBorder(BorderFactory.createTitledBorder("Original Text"));

        // ---- Output area ----
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Rewritten Text"));

        // ---- Mode dropdown ----
        String[] modes = {"Creative", "Academic", "Professional"};
        modeDropdown = new JComboBox<>(modes);

        // ---- Rewrite button ----
        JButton rewriteButton = new JButton("Rewrite");
        rewriteButton.addActionListener(e -> {
            String text = inputArea.getText();
            String mode = (String) modeDropdown.getSelectedItem();
            controller.handleRewriteRequest(text, mode);
        });

        // ---- Top panel (mode selection + button) ----
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(modeDropdown);
        topPanel.add(rewriteButton);

        // ---- Add everything to panel ----
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(inputArea), BorderLayout.WEST);
        add(new JScrollPane(outputArea), BorderLayout.EAST);
    }

    /** Called by controller to update the result area */
    public void setOutputText(String text) {
        outputArea.setText(text);
    }
}
