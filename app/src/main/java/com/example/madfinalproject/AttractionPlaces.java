package com.example.madfinalproject;

import java.util.ArrayList;
import java.util.HashMap;

public class AttractionPlaces {
    private String name;
    private String address;
    private String description;
    private String city;
    private HashMap<String, String> images;

    public AttractionPlaces() {
    }

    public AttractionPlaces(String name, String address, String description, String city, HashMap<String, String> images) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.city = city;
        this.images = images;
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
