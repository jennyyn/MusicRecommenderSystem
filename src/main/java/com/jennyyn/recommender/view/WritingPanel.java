package com.jennyyn.recommender.view;

import com.jennyyn.recommender.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class WritingPanel extends JPanel {

    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JComboBox<String> modeDropdown;

    private MainController controller;

    public WritingPanel() {
        setLayout(new BorderLayout());

        // ---- Input area ----
        inputArea = new JTextArea(8, 40);
        inputArea.setBorder(BorderFactory.createTitledBorder("Original Text"));
        inputArea.setLineWrap(true);        // enable line wrapping
        inputArea.setWrapStyleWord(true);   // wrap at word boundaries

        // ---- Output area ----
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Rewritten Text"));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

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

        // ---- Bottom right: Save + Load ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        saveButton.addActionListener(e -> controller.handleSaveRequest());
        loadButton.addActionListener(e -> controller.handleLoadRequest());

        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);

        // ---- Center panel (stack input/output vertically) ----
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(new JScrollPane(inputArea));
        centerPanel.add(new JScrollPane(outputArea));

        // ---- Add everything ----
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // shows the session
    public int showSessionSelectionDialog(String[] options) {
        return JOptionPane.showOptionDialog(
                this,
                "Select a session to load:",
                "Load Session",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    }


    public void setController(MainController controller) {
        this.controller = controller;
    }

    public String getOriginalText() {
        return inputArea.getText();
    }

    public void setOriginalText(String text) {
        inputArea.setText(text);
    }

    public String getRewrittenText() {
        return outputArea.getText();
    }

    public void setRewrittenText(String text) {
        outputArea.setText(text);
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }

}
