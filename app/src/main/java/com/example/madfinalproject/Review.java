package com.example.madfinalproject;

public class Review {
    private float rateValue;
    private String review;

    public Review() {
    }

    public Review(float rateValue, String review) {
        this.rateValue = rateValue;
        this.review = review;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
