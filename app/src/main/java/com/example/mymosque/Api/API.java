package com.example.mymosque.Api;

import com.example.mymosque.Model.AskImam;
import com.example.mymosque.Model.Favourite;
import com.example.mymosque.Model.MasajidList;
import com.example.mymosque.Model.Message;
import com.example.mymosque.Model.PrimaryMosque;
import com.example.mymosque.Model.Primarymosquedata;
import com.example.mymosque.Model.USERID;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {



    //get all schemes
    @GET("getMosquesList/{userid}")
    Call<MasajidList> getMosquesList(
            @Path("userid") int userid);




    //get all schemes
    @GET("farvoriate/{userid}")
    Call<MasajidList> getFavoriteList(
            @Path("userid") int userid);




    //SetFavouriteFunction call
    @FormUrlEncoded
    @POST("Question")
    Call<ArrayList<AskImam>> AskImam(
            @Field("m_id") int  m_id,
            @Field("u_id") int  u_id,
            @Field("question") String  question
    );


    //SetFavouriteFunction call
    @FormUrlEncoded
    @POST("setprimary")
    Call<PrimaryMosque> SetPrimary(
            @Field("u_id") int  u_id,
            @Field("m_id") int  m_id
    );


    //SetFavouriteFunction call
    @FormUrlEncoded
    @POST("getprimarymosque")
    Call<Primarymosquedata> getPrimary(
            @Field("userid") int  u_id
    );




    //Create User Function Call
    @FormUrlEncoded
    @POST("createuser")
    Call<USERID> GetUserID(
            @Field("emi") String emi
    );



    //SetFavouriteFunction call
    @FormUrlEncoded
    @POST("farvoriate")
    Call<Favourite> SetFavourite(
            @Field("m_id") int  m_id,
            @Field("u_id") int  u_id

    );






    @FormUrlEncoded
    @POST("farvoriate/{id}")
    Call<Favourite> Unfavourite(
            @Path("id") int id,
            @Field("favoriate")  int favoriate
    );



    @FormUrlEncoded
    @POST("feedback/{u_id}")
    Call<Message> Feedback(
            @Path("u_id") int id,
            @Field("msg")  String message,
            @Field("contact")  String contact,
            @Field("name")  String name
    );



    @FormUrlEncoded
    @POST("requestmosque")
    Call<String> addmosque(
            @Field("pname")  String pname,
            @Field("mname")  String mname,
            @Field("contact")  String contact
    );

}
