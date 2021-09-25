package com.example.madfinalproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Package implements Parcelable {

    private String name;
    private String nuGuest;
    private String fee;
    private String description;
    //private boolean isCheck='True';
    private ArrayList<String> amenities;
    private ArrayList<String> amenities2;
    private String uuid;
    //private String refundable;

    public Package() {
    }

    public Package(String name, String nuGuest, String fee, String description, ArrayList<String> amenities, ArrayList<String> amenities2, String uuid) {
        this.name = name;
        this.nuGuest = nuGuest;
        this.fee = fee;
        this.description = description;
        this.amenities = amenities;
        this.amenities2 = amenities2;
        this.uuid = uuid;
    }

    protected Package(Parcel in) {
        name = in.readString();
        nuGuest = in.readString();
        fee = in.readString();
        description = in.readString();
        amenities = in.createStringArrayList();
        amenities2 = in.createStringArrayList();
        uuid = in.readString();
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };

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

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public ArrayList<String> getAmenities2() {
        return amenities2;
    }

    public void setAmenities2(ArrayList<String> amenities2) {
        this.amenities2 = amenities2;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(nuGuest);
        parcel.writeString(fee);
        parcel.writeString(description);
        parcel.writeStringList(amenities);
        parcel.writeStringList(amenities2);
        parcel.writeString(uuid);
    }
}
