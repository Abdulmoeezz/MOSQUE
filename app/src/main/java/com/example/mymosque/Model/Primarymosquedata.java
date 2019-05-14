package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class Primarymosquedata {















/*
 "address": "16 Crown Terrace",
         "farvoriate": 1,
         "farvoriate_id": 1
}

*/



    @SerializedName("ID")
    private int ID;

    @SerializedName("name")
    private String name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFarvoriate() {
        return farvoriate;
    }

    public void setFarvoriate(int farvoriate) {
        this.farvoriate = farvoriate;
    }

    public int getFarvoriate_id() {
        return farvoriate_id;
    }

    public void setFarvoriate_id(int farvoriate_id) {
        this.farvoriate_id = farvoriate_id;
    }

    @SerializedName("longtitude")
    private String longtitude;


    @SerializedName("latitude")
    private String latitude;

    @SerializedName("imageurl")
    private String imageurl;

    @SerializedName("address")
    private String address;






    @SerializedName("farvoriate")
    private int farvoriate;


    @SerializedName("farvoriate_id")
    private int farvoriate_id;

    public String getTopics_name() {
        return topics_name;
    }

    public void setTopics_name(String topics_name) {
        this.topics_name = topics_name;
    }

    public Primarymosquedata(int ID, String name, String longtitude, String latitude, String imageurl, String address, int farvoriate, int farvoriate_id, String topics_name) {
        this.ID = ID;
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.imageurl = imageurl;
        this.address = address;
        this.farvoriate = farvoriate;
        this.farvoriate_id = farvoriate_id;
        this.topics_name = topics_name;
    }

    @SerializedName("topics_name")
    private String topics_name;


}
