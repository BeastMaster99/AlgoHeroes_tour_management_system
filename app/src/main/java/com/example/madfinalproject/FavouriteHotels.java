package com.example.madfinalproject;

public class FavouriteHotels {
    private String favId;
    private String userEmail;
    private String hotelId;

    public FavouriteHotels(String favId, String userEmail, String hotelId) {
        this.favId = favId;
        this.userEmail = userEmail;
        this.hotelId = hotelId;
    }

    public FavouriteHotels() {
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }
}
