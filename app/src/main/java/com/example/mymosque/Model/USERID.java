package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class USERID {

    @SerializedName("emi_number")
    private String emi_number;

    @SerializedName("u_id")
    private int u_id;



    public USERID (String emi_number, int user_id) {
        this.emi_number = emi_number;
        this.u_id = user_id;
    }


    public String getEmi_number() {
        return emi_number;
    }

    public int getu_id() {
        return u_id;
    }

}
