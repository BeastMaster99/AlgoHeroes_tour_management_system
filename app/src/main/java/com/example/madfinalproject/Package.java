package com.example.madfinalproject;
import java.util.ArrayList;

public class Package  {

    private String name;
    private String nuGuest;
    private String fee;
    private String description;
    //private boolean isCheck='True';
    private ArrayList<String> roomFeatures;
    private ArrayList<String> roomTypes;
    private String uuid;
    //private String refundable;

    public Package() {
    }

    public Package(String name, String nuGuest, String fee, String description, ArrayList<String> roomFeatures, ArrayList<String> roomTypes, String uuid) {
        this.name = name;
        this.nuGuest = nuGuest;
        this.fee = fee;
        this.description = description;
        this.roomFeatures = roomFeatures;
        this.roomTypes = roomTypes;
        this.uuid = uuid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNuGuest() {
        return nuGuest;
    }

    public void setNuGuest(String nuGuest) {
        this.nuGuest = nuGuest;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getRoomFeatures() {
        return roomFeatures;
    }

    public void setRoomFeatures(ArrayList<String> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }

    public ArrayList<String> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ArrayList<String> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Package{" +
                "name='" + name + '\'' +
                ", nuGuest='" + nuGuest + '\'' +
                ", fee='" + fee + '\'' +
                ", description='" + description + '\'' +
                ", roomFeatures=" + roomFeatures +
                ", roomTypes=" + roomTypes +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
