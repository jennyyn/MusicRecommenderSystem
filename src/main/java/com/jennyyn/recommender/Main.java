package com.jennyyn.recommender;

import com.jennyyn.recommender.view.MainFrame;

public class  Main {
    public static void main(String[] args) {
        // Start the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
