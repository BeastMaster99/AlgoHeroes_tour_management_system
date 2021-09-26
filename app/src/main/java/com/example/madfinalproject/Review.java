package com.example.madfinalproject;

public class Review {
    private float rateValue;
    private String review;
    private String hotelId;
    private String travelerId;
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

    public Review(float rateValue, String review, String hotelId, String travelerId, String reviewId, String fullName) {
        this.rateValue = rateValue;
        this.review = review;
        this.hotelId = hotelId;
        this.travelerId = travelerId;
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

    public String getTravelerId() {
        return travelerId;
    }

    public void setTravelerId(String traverId) {
        this.travelerId = traverId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}
