package com.example.madfinalproject;

import java.util.ArrayList;
import java.util.HashMap;

public class Hotel {
    private String name;
    private String owner;
    private String address;
    private String contact;
    private String description;
    private String city;
    private ArrayList<String> amenities;
    private HashMap<String, String> images;

    public Hotel() {
    }

    public Hotel(String name, String owner, String address, String contact, String description, String city, ArrayList<String> amenities, HashMap<String, String> images) {
        this.name = name;
        this.owner = owner;
        this.address = address;
        this.contact = contact;
        this.description = description;
        this.city = city;
        this.amenities = amenities;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public HashMap<String, String> getImages() {
        return images;
    }

    public void setImages(HashMap<String, String> images) {
        this.images = images;
    }
}