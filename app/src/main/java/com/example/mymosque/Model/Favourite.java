package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class Favourite {


    @SerializedName("ID")
    private int ID;

    @SerializedName("name")
    private String name;

    @SerializedName("longtitude")
    private String longtitude;

    @SerializedName("latitude")
    private String latitude;


    @SerializedName("imageurl")
    private String imageurl;


    @SerializedName("farvoriate")
    private int farvoriate;

    public Favourite(int ID, String name, String longtitude, String latitude, String imageurl, int farvoriate, int farvoriate_id) {
        this.ID = ID;
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.imageurl = imageurl;
        this.farvoriate = farvoriate;
        this.farvoriate_id = farvoriate_id;
    }

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

    @SerializedName("farvoriate_id")
    private int farvoriate_id;







}
