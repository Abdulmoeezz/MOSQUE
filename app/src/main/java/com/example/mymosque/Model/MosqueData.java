package com.example.mymosque.Model;

public class MosqueData {

             /*"ID": 6,
             "name": "new Imam",
             "longtitude": "022020",
             "latitude": "123321",
             "imageurl": "http://masjidi.co.uk/panel/userpanel/uploads/mosque_pic/1548597934.jpg",
             "farvoriate": 1,
             "farvoriate_id": 42*/

             private int ID ,farvoriate,farvoriate_id;
             private String name;
    private String longtitude;
    private String latitude;
    private String imageurl;
    private double miles ;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;


    public MosqueData() {
    }

    public MosqueData(int ID, int farvoriate, int farvoriate_id, String name, String longtitude, String latitude, String imageurl,String address,double miles) {
        this.ID = ID;
        this.farvoriate = farvoriate;
        this.farvoriate_id = farvoriate_id;
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.imageurl = imageurl;
        this.address=address;
        this.miles = miles ;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }
}
