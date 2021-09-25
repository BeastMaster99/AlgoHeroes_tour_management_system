package com.example.madfinalproject;

public class HotelBookings {
    private String hotelName;
    private String hotelId;
    private String travelerEmail;
    private String hotelOwnerEmail;
    private String travelerContactNumber;
    private String travelerFirstName;
    private String checkInDate;
    private String checkOutDate;
    private String numberOfRooms;
    private String extraDetails;

    public HotelBookings() {
    }

    public HotelBookings(String hotelName, String hotelId, String travelerEmail, String hotelOwnerEmail, String travelerContactNumber, String travelerFirstName, String checkInDate, String checkOutDate, String numberOfRooms, String extraDetails) {
        this.hotelName = hotelName;
        this.hotelId = hotelId;
        this.travelerEmail = travelerEmail;
        this.hotelOwnerEmail = hotelOwnerEmail;
        this.travelerContactNumber = travelerContactNumber;
        this.travelerFirstName = travelerFirstName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
        this.extraDetails = extraDetails;
    }

    public String getHotelOwnerEmail() {
        return hotelOwnerEmail;
    }

    public void setHotelOwnerEmail(String hotelOwnerEmail) {
        this.hotelOwnerEmail = hotelOwnerEmail;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getTravelerEmail() {
        return travelerEmail;
    }

    public void setTravelerEmail(String travelerEmail) {
        this.travelerEmail = travelerEmail;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String getTravelerContactNumber() {
        return travelerContactNumber;
    }

    public void setTravelerContactNumber(String travelerContactNumber) {
        this.travelerContactNumber = travelerContactNumber;
    }

    public String getTravelerFirstName() {
        return travelerFirstName;
    }

    public void setTravelerFirstName(String travelerFirstName) {
        this.travelerFirstName = travelerFirstName;
    }

}

