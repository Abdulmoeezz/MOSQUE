package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class PrimaryMosque {


    @SerializedName("u_id")
    private int u_id;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;


    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;


    @SerializedName("longtitude")
    private String longtitude;


    @SerializedName("latitude")
    private String latitude;


    @SerializedName("is_active")
    private String is_active;


    @SerializedName("timestamp")
    private String timestamp;



    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("remember_token")
    private String remember_token;


    @SerializedName("emi_number")
    private String emi_number;


    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public PrimaryMosque(int u_id, String fname, String lname, String phone_number, String email, String address, String longtitude, String latitude, String is_active, String timestamp, String username, String password, String remember_token, String emi_number, String topic_name, int primary_mosque) {
        this.u_id = u_id;
        this.fname = fname;
        this.lname = lname;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.is_active = is_active;
        this.timestamp = timestamp;
        this.username = username;
        this.password = password;
        this.remember_token = remember_token;
        this.emi_number = emi_number;
        this.topic_name = topic_name;
        this.primary_mosque = primary_mosque;
    }

    @SerializedName("topic_name")
    private String topic_name;


    public PrimaryMosque(int u_id, String fname, String lname, String phone_number, String email, String address, String longtitude, String latitude, String is_active, String timestamp, String username, String password, String remember_token, String emi_number, int primary_mosque) {
        this.u_id = u_id;
        this.fname = fname;
        this.lname = lname;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.is_active = is_active;
        this.timestamp = timestamp;
        this.username = username;
        this.password = password;
        this.remember_token = remember_token;
        this.emi_number = emi_number;
        this.primary_mosque = primary_mosque;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getEmi_number() {
        return emi_number;
    }

    public void setEmi_number(String emi_number) {
        this.emi_number = emi_number;
    }

    public int getPrimary_mosque() {
        return primary_mosque;
    }

    public void setPrimary_mosque(int primary_mosque) {
        this.primary_mosque = primary_mosque;
    }

    @SerializedName("primary_mosque")
    private int primary_mosque;










}
