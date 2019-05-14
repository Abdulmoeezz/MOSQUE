package com.example.mymosque;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Fragments.FragmentPrayerTimes;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteProfileFragment extends Fragment {

    View v;
    String FavouriteMosqueID,FavouriteMosqueName,FavouriteMosqueImageUrl,FavouriteMosqueAddres,FavouriteMosqueMiles,Flongitude,Flatitude;
    ImageView MosqueImage;
    TextView  MosqueText,MosqueAddress,MosqueMiles;
    Button    ViewOnMapBTN,ViewMosquePyarTimesBTN;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.favourite_profile_fragment, container, false);
        //For Toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Favourite Mosque Profile");
        //For Toolbar

        MosqueImage=v.findViewById(R.id.img_);
        MosqueText=v.findViewById(R.id.HeadingTxt1);
        ViewMosquePyarTimesBTN=v.findViewById(R.id.PrayerTimesBTN);
        ViewOnMapBTN=v.findViewById(R.id.VIewonMapBTN);
        MosqueAddress=v.findViewById(R.id.addressTxt2);
        MosqueMiles=v.findViewById(R.id.Txt_Miles);


        SharedPreferences prefs = getActivity().getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE);
        FavouriteMosqueName=prefs.getString("M_name","not Defined");
        FavouriteMosqueImageUrl=prefs.getString("M_Image_","not Defined");
        FavouriteMosqueID=prefs.getString("M_ID","not Defined");
        FavouriteMosqueAddres=prefs.getString("M_Address_","not Defined");
        FavouriteMosqueMiles=prefs.getString("miles","not Defined");
        Flongitude=prefs.getString("M_Longitude_","not Defined");
        Flatitude=prefs.getString("M_Latitude_","not Defined");





        SetViews();

        return v;
    }//End onCreateView Method


 


public  void  SetViews(){


    Picasso.get().load(FavouriteMosqueImageUrl).into(MosqueImage, new com.squareup.picasso.Callback() {
        @Override
        public void onSuccess() {


        }
        @Override
        public void onError(Exception e) {
            Toast.makeText(getActivity(), "No Map Found !!", Toast.LENGTH_SHORT).show();
        }

    });
    MosqueText.setText(FavouriteMosqueName);
    MosqueAddress.setText(FavouriteMosqueAddres);
    MosqueMiles.setText(FavouriteMosqueMiles);

    //Call Buttons;
    ViewMosquePyarTimesBTN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentPrayerTimes()).commit();

        }
    });

    ViewOnMapBTN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Location", MODE_PRIVATE).edit();
            editor.putString("Longitude",Flongitude);
            editor.putString("Latitude", Flatitude);
            editor.apply();



            Intent intent= new Intent(getActivity(), MapsActivity.class);
            getActivity().startActivity(intent);






        }
    });






}











}
