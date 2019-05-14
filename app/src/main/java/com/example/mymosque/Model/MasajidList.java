package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasajidList {


    @SerializedName("data")
    private ArrayList<Masjid> MasajidListArrayList;

    public MasajidList() {
    }

    public ArrayList<Masjid> getMasajidListArrayList() {
        return MasajidListArrayList;
    }








}
