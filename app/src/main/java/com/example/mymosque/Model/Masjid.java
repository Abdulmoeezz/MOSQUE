package com.example.mymosque.Model;

public class Masjid {



    private int ID ;
    private String  name;
    private String  longtitude;
    private String  latitude;
    private String  image_name;
    private String  image_path;
    private int  forvoriate_id;
    private int  forvoriate;
   private links links;
   private meta  meta;




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

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getForvoriate_id() {
        return forvoriate_id;
    }

    public void setForvoriate_id(int forvoriate_id) {
        this.forvoriate_id = forvoriate_id;
    }

    public int getForvoriate() {
        return forvoriate;
    }

    public void setForvoriate(int forvoriate) {
        this.forvoriate = forvoriate;
    }







 /* "links": {
        "first": "http://masjidi.co.uk/api/getMosquesList/33?page=1",
                "last": "http://masjidi.co.uk/api/getMosquesList/33?page=7",
                "prev": null,
                "next": "http://masjidi.co.uk/api/getMosquesList/33?page=2"
    },

*/






}
