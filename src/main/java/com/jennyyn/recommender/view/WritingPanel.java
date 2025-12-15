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
    public void showSessionListWindow(
            String[] options,
            Runnable onLoad,
            Runnable onDelete
    ) {
        JDialog dialog = new JDialog((Frame) null, "Session History", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String s : options) listModel.addElement(s);

        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons (hidden until selection)
        JButton loadBtn = new JButton("Load Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        loadBtn.setVisible(false);
        deleteBtn.setVisible(false);

        // Show buttons when user selects something
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selected = list.getSelectedIndex() >= 0;
                loadBtn.setVisible(selected);
                deleteBtn.setVisible(selected);
            }
        });

        // Bottom button panel
        JPanel bottom = new JPanel();
        bottom.add(loadBtn);
        bottom.add(deleteBtn);
        dialog.add(bottom, BorderLayout.SOUTH);

        // Button actions call Controller callbacks
        loadBtn.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                WritingPanel.this.putClientProperty("selectedIndex", index);
                onLoad.run();
                dialog.dispose();
            }
        });

        deleteBtn.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                WritingPanel.this.putClientProperty("selectedIndex", index);
                onDelete.run();
                listModel.remove(index);
                loadBtn.setVisible(false);
                deleteBtn.setVisible(false);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
