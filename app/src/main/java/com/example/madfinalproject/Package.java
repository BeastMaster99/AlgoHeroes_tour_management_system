package com.example.madfinalproject;
import java.io.Serializable;
import java.util.ArrayList;

public class Package implements Serializable {

    private String name;
    private String nuGuest;
    private String fee;
    private String description;
    private ArrayList<String> roomFeatures;
    private ArrayList<String> roomTypes;
    private String nRooms;
    private String uuid;
    private String hotelId;

    //private String rating;

    public Package() {
    }

    public Package(String name, String nuGuest, String fee, String description, ArrayList<String> roomFeatures, ArrayList<String> roomTypes, String nRooms, String uuid, String hotelId) {
        this.name = name;
        this.nuGuest = nuGuest;
        this.fee = fee;
        this.description = description;
        this.roomFeatures = roomFeatures;
        this.roomTypes = roomTypes;
        this.nRooms = nRooms;
        this.uuid = uuid;
        this.hotelId = hotelId;


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

    public String getnRooms() {
        return nRooms;
    }

    public void setnRooms(String nRooms) {
        this.nRooms = nRooms;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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
