package com.example.mymosque.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.MapsActivity;
import com.example.mymosque.Model.Favourite;
import com.example.mymosque.Model.PrimaryMosque;
import com.example.mymosque.Model.Primarymosquedata;
import com.example.mymosque.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class FragementProfile extends Fragment {


     View v;
     ImageView MosqueImage;
     TextView  MosqueName ,MosqueAddress,MosqueMiles;
     String mosqueImage,mosquename,mosqueID,PrimaryMosQueID,mosqueaddress,mosquemiles,restoredText,longitude,latitude;
     SharedPreferences.Editor editor,EditPerivous;
     Button AskImam,mosqueBtn,ParyerTimes,ViewOnMap;
     String UserID,TopicName,PreviousTopicName;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditPerivous = getActivity().getSharedPreferences("GetPreviousMosque", MODE_PRIVATE).edit();









        SharedPreferences prefs = getActivity().getSharedPreferences("PassMosqueData", MODE_PRIVATE);
        mosquename=prefs.getString("M_name","not Defined");
        mosqueImage=prefs.getString("M_Image_","not Defined");
        mosqueID=prefs.getString("M_ID","not Defined");
        mosqueaddress=prefs.getString("M_address_","not Defined");
        mosquemiles=prefs.getString("M_Miles_","not Defined");
        longitude=prefs.getString("M_Longitude_","not Defined");
        latitude=prefs.getString("M_Latitude_","not Defined");




    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);


        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PrimaryMosQueID = prefss.getString("PM_ID", null);


        SharedPreferences UserPerfs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(UserPerfs.getInt("ID", 0));



        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Profile");

        InitViews();
        try{
            if(PrimaryMosQueID.equals(mosqueID)) {

                AskImam.setVisibility(View.VISIBLE);
                mosqueBtn.setVisibility(View.GONE);

            }else {
                    AskImam.setVisibility(View.INVISIBLE);
                    ParyerTimes.setVisibility(View.INVISIBLE);
            }





            }catch (Exception ex){

            AskImam.setVisibility(View.INVISIBLE);
            ParyerTimes.setVisibility(View.INVISIBLE);


        }




        //</For Toolbar>

        mosqueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* AppCompatActivity activity = (AppCompatActivity) v.getContext();
                 activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentMyMasjid()).commit();*/



                if(PrimaryMosQueID == null){

                    EditPerivous.putString("PreMosque_ID","");
                    EditPerivous.apply();
                    SetPrimaryMosque(Integer.parseInt(UserID), Integer.parseInt(mosqueID));

                }else {


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("you Already Done One MosQue As Primary if want to this Mosque is PrimaryMosQue Click on Yes");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {



                                    EditPerivous.putString("PreMosque_ID","OK");
                                    EditPerivous.apply();
                                    dialog.cancel();

                                    SetPrimaryMosque(Integer.parseInt(UserID), Integer.parseInt(mosqueID));

                                    AskImam.setVisibility(View.VISIBLE);
                                    ParyerTimes.setVisibility(View.VISIBLE);
                                    mosqueBtn.setVisibility(View.GONE);


                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }
        });
        SharedPreferences UserPrefs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        AskImam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentAskImam()).commit();
            }
        });
        ParyerTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentPrayerTimes()).commit();
  }
        });
        ViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Location", MODE_PRIVATE).edit();
                editor.putString("Latitude",latitude);
                editor.putString("Longitude", longitude);
                editor.apply();


                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);



            }
        });




        return v;
    }//End onCreateView Method




public void InitViews(){

        MosqueImage= v.findViewById(R.id.img_);
        MosqueName=v.findViewById(R.id.HeadingTxt1);
        MosqueName.setText(mosquename);

        mosqueBtn = (Button) v.findViewById(R.id.MosqueBTN);
        AskImam= (Button) v.findViewById(R.id.AskImamBTN);
        ParyerTimes=(Button)v.findViewById(R.id.PrayerTimesBTN);
        ViewOnMap=(Button)v.findViewById(R.id.VIewonMapBTN);
        MosqueAddress=v.findViewById(R.id.addressTxt2);
        MosqueMiles=v.findViewById(R.id.Txt_Miles);

        MosqueAddress.setText(mosqueaddress);
        MosqueMiles.setText(mosquemiles);


        Picasso.get().load(mosqueImage).into(MosqueImage, new com.squareup.picasso.Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Exception e) {
            Toast.makeText(getActivity(),"ON Image Attach!!",Toast.LENGTH_SHORT).show();
        }


    });



}

    public  void  SetPrimaryMosque(int U,int M){



        SharedPreferences TopicNamePrimaryMosque=getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
         PreviousTopicName=TopicNamePrimaryMosque.getString("PM_TopicsName","never");


         if(!PreviousTopicName.equals("never")){
             FirebaseMessaging.getInstance().unsubscribeFromTopic(PreviousTopicName);
         }



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<PrimaryMosque> call = service.SetPrimary(U,M);

        call.enqueue(new Callback<PrimaryMosque>() {
            @Override
            public void onResponse(Call<PrimaryMosque> call, Response<PrimaryMosque> response) {


                Toast.makeText(getActivity(),"SET Mosque: "+response.body().getPrimary_mosque(),Toast.LENGTH_LONG).show();
            /*    editor = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE).edit();
                editor.putString("PM_ID", String.valueOf(response.body().getPrimary_mosque()));
                editor.putString("PM_NAME",mosquename);
                editor.putString("PM_URL",mosqueImage);
                editor.putString("PM_Address",mosqueaddress);
                editor.putString("PM_Milesaway",mosquemiles);
                editor.putString("PM_longitude",longitude);
                editor.putString("PM_latitude",latitude);
                editor.putString("PM_TopicName",response.body().getTopic_name());
                editor.apply();
*/


                GetPrimaryMosQueID(U);

                AskImam.setVisibility(View.VISIBLE);
                ParyerTimes.setVisibility(View.VISIBLE);
                mosqueBtn.setVisibility(View.GONE);






            }

            @Override
            public void onFailure(Call<PrimaryMosque> call, Throwable t) {


                Toast.makeText(getActivity(),"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });










    }//End of Function
    public  void  GetPrimaryMosQueID(int U){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<Primarymosquedata> call = service.getPrimary(U);

        call.enqueue(new Callback<Primarymosquedata>() {
            @Override
            public void onResponse(Call<Primarymosquedata> call, Response<Primarymosquedata> response) {

                try {

                    Toast.makeText(getActivity(), "SS  Primary MosQue ID:" + response.body().getID(), Toast.LENGTH_LONG).show();
                 /*   SharedPreferences.Editor editor;
                    editor = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE).edit();
                    editor.putString("PM_ID", String.valueOf(response.body().getID()));
                    editor.putString("PM_NAME", response.body().getName());
                    editor.putString("PM_URL", response.body().getImageurl());
                    editor.putString("PM_Address", response.body().getAddress());
                    editor.putString("PM_Milesaway", "");
                    editor.putString("PM_longitude", response.body().getLongtitude());
                    editor.putString("PM_latitude", response.body().getLatitude());
                    editor.putString("PM_TopicsName", response.body().getTopics_name());
                    editor.apply();*/



                    editor = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE).edit();
                    editor.putString("PM_ID", String.valueOf(response.body().getID()));
                    editor.putString("PM_NAME",mosquename);
                    editor.putString("PM_URL",mosqueImage);
                    editor.putString("PM_Address",mosqueaddress);
                    editor.putString("PM_Milesaway",mosquemiles);
                    editor.putString("PM_longitude",longitude);
                    editor.putString("PM_latitude",latitude);
                    editor.putString("PM_TopicsName", response.body().getTopics_name());
                    editor.apply();


                    TopicName=response.body().getTopics_name();
                    try {
                    FirebaseMessaging.getInstance().subscribeToTopic(TopicName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                          //  Toast.makeText(getActivity(),"FP:Success",Toast.LENGTH_LONG).show();
                        }
                    });



    FirebaseMessaging.getInstance().subscribeToTopic("admin").addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
           // Toast.makeText(getActivity(), "FP:Success", Toast.LENGTH_LONG).show();
        }
    });


}catch (NullPointerException ex){



}












                }catch (NullPointerException ex){
                    ex.printStackTrace();


                }



            }

            @Override
            public void onFailure(Call<Primarymosquedata> call, Throwable t) {
                Toast.makeText(getActivity(),"SS   Server Problem Contact to System Support",Toast.LENGTH_LONG).show(); }
        }); }



}
