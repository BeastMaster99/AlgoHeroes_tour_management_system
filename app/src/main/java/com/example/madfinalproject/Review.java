package com.example.madfinalproject;

public class Review {
    private float rateValue;
    private String review;
    private String hotelId;
    private String traverId;
    private String reviewId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;

    public Review() {
    }

    public Review(float rateValue, String review, String hotelId, String traverId, String reviewId, String fullName) {
        this.rateValue = rateValue;
        this.review = review;
        this.hotelId = hotelId;
        this.traverId = traverId;
        this.reviewId = reviewId;
        this.fullName = fullName;
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

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getTraverId() {
        return traverId;
    }

    public void setTraverId(String traverId) {
        this.traverId = traverId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}
