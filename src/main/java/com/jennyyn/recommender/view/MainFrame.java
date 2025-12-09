package com.jennyyn.recommender.view;


import com.jennyyn.recommender.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final WritingPanel writingPanel;

    public MainFrame() {
        super("Writing Assistant");

        // Create controller AFTER UI exists
        MainController controller = new MainController(this);

        // Create main panel and pass controller to it
        writingPanel = new WritingPanel(controller);

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

