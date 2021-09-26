package com.example.madfinalproject;

import java.util.HashMap;

public class AttractionPlaces {
    private String placeId;
    private String tourGuide;
    private String name;
    private String address;
    private String description;
    private String city;
    private HashMap<String, String> images;

    public AttractionPlaces() {
    }

    public AttractionPlaces(String placeId, String tourGuide, String name, String address, String description, String city, HashMap<String, String> images) {
        this.placeId = placeId;
        this.tourGuide = tourGuide;
        this.name = name;
        this.address = address;
        this.description = description;
        this.city = city;
        this.images = images;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTourGuide() {  return tourGuide; }

    public void setTourGuide(String tourGuide) {
        this.tourGuide = tourGuide;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public HashMap<String, String> getImages() { return images; }

    public void setImages(HashMap<String, String> images) { this.images = images; }

}
