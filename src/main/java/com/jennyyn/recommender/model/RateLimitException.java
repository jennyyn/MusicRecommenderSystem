package com.jennyyn.recommender.model;

public class RateLimitException extends Exception {
    public RateLimitException(String message) {
        super(message);
    }
}
