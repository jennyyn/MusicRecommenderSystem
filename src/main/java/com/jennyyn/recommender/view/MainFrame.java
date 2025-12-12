package com.jennyyn.recommender.view;


import com.jennyyn.recommender.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final WritingPanel writingPanel;
    private final MainController controller;

    public MainFrame() {
        super("Writing Assistant");

        // Create WritingPanel first
        writingPanel = new WritingPanel();

        //Create controller and pass references
        controller = new MainController(this, writingPanel);

        //Put controller into WritingPanel
        writingPanel.setController(controller);

        setLayout(new BorderLayout());
        add(writingPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Called by the controller to display rewritten text */
    public void displayResult(String rewrittenText) {
        writingPanel.setOutputText(rewrittenText);
    }
}

