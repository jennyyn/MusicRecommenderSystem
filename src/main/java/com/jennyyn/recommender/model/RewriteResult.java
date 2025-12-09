package com.jennyyn.recommender.model;

public class RewriteResult {

    private final String rewrittenText;

    public RewriteResult(String rewrittenText) {
        this.rewrittenText = rewrittenText;
    }

    public String getRewrittenText() {
        return rewrittenText;
    }

    @Override
    public String toString() {
        return rewrittenText;
    }
}

