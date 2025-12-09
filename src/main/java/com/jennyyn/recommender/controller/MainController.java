package com.jennyyn.recommender.controller;


import com.jennyyn.recommender.model.*;
import com.jennyyn.recommender.service.APIService;
import com.jennyyn.recommender.view.MainFrame;

public class MainController {

    private final APIService apiService;
    private final MainFrame mainFrame;

    public MainController(MainFrame mainFrame) {
        this.apiService = new APIService();
        this.mainFrame = mainFrame;
    }

    public void handleRewriteRequest(String text, String mode) {

        WritingStrategy strategy;

        switch (mode) {
            case "Creative":
                strategy = new CreativeStrategy();
                break;
            case "Academic":
                strategy = new AcademicStrategy();
                break;
            case "Professional":
                strategy = new ProfessionalStrategy();
                break;
            default:
                strategy = new CreativeStrategy(); // fallback
        }

        RewriteResult result = apiService.rewriteText(text, strategy);

        // update UI
        mainFrame.displayResult(result.getRewrittenText());
    }
}
