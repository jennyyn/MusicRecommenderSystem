package com.jennyyn.recommender.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrategyTest {

    @Test
    public void testCreativeStrategyPrompt() {
        WritingStrategy strategy = new CreativeStrategy();
        String prompt = strategy.buildPrompt("Hello world");

        assertTrue(prompt.toLowerCase().contains("creative"));
        assertTrue(prompt.contains("Hello world"));
    }

    @Test
    public void testAcademicStrategyPrompt() {
        WritingStrategy strategy = new AcademicStrategy();
        String prompt = strategy.buildPrompt("Hello world");

        assertTrue(prompt.toLowerCase().contains("academic")
                || prompt.toLowerCase().contains("formal"));
        assertTrue(prompt.contains("Hello world"));
    }

    @Test
    public void testProfessionalStrategyPrompt() {
        WritingStrategy strategy = new ProfessionalStrategy();
        String prompt = strategy.buildPrompt("Hello world");

        assertTrue(prompt.toLowerCase().contains("professional")
                || prompt.toLowerCase().contains("business"));
        assertTrue(prompt.contains("Hello world"));
    }

    @Test
    public void testRewriteResultStoresText() {
        RewriteResult result = new RewriteResult("Rewritten");
        assertEquals("Rewritten", result.getRewrittenText());
    }
}

