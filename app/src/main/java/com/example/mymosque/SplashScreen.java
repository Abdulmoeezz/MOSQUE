package com.example.mymosque;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.Model.Primarymosquedata;
import com.example.mymosque.Model.USERID;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity implements OnRequestPermissionsResultCallback {

    String StrPhoneType = "";
    String EMInumber;
    final static int permission_read_state_ = 123;
    int ID, UserID=0;
    SharedPreferences.Editor editor;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Hide Actionbar and For full screen
        //window
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Hide Actionbar and For full screen

        SharedPreferences prefs = getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID= prefs.getInt("ID", 0);



        //Check Permission
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {
            if (UserID == 0) {
                MyTelephoneManger();
            } else {
                try {
                    GetPrimaryMosQueID(UserID);
                    GotoNextScreen();
                } catch (Exception ex) {}
            }
        }






    }//end of OnCreate Method


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyTelephoneManger();
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void MyTelephoneManger() {
        TelephonyManager telecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        assert telecomManager != null;
        int PhoneType = telecomManager.getPhoneType();
        switch (PhoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                StrPhoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                StrPhoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                StrPhoneType = "NONE";
                break; }

                boolean isRoaming = telecomManager.isNetworkRoaming();
        String Phonetype = StrPhoneType;
        EMInumber = telecomManager.getDeviceId();
      //  Toast.makeText(SplashScreen.this, "Get Your EMI Number:" + EMInumber +"For Making You User ", Toast.LENGTH_SHORT).show();
        SetEMINumberIntoServer();




    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

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
                  //  Toast.makeText(getApplicationContext(), "SS  Primary MosQue ID:" + response.body().getID(), Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor;
                    editor = getApplicationContext().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE).edit();
                    editor.putString("PM_ID", String.valueOf(response.body().getID()));
                    editor.putString("PM_NAME", response.body().getName());
                    editor.putString("PM_URL", response.body().getImageurl());
                    editor.putString("PM_Address", response.body().getAddress());
                    editor.putString("PM_Milesaway", "");
                    editor.putString("PM_longitude", response.body().getLongtitude());
                    editor.putString("PM_latitude", response.body().getLatitude());
                    editor.putString("PM_TopicsName", response.body().getTopics_name());
                    editor.apply();


                }catch (NullPointerException ex){
                    ex.printStackTrace();


                }



                }

            @Override
            public void onFailure(Call<Primarymosquedata> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"SS: Server Problem Contact to System Support",Toast.LENGTH_LONG).show(); }
        }); }
    public void SetEMINumberIntoServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<USERID> call = service.GetUserID(EMInumber);

        call.enqueue(new Callback<USERID>() {
            @Override
            public void onResponse(Call<USERID> call, Response<USERID> response) {

                try {
                    ID = response.body().getu_id();
                    editor = getSharedPreferences("My_SP", MODE_PRIVATE).edit();
                    editor.putInt("ID", ID);
                    editor.apply();
                   // Toast.makeText(getApplicationContext(), "UserID_" + "=" + ID, Toast.LENGTH_LONG).show();
                    GetPrimaryMosQueID(ID);
                    GotoNextScreen();

                } catch (NullPointerException ex) {

                    ex.printStackTrace();

                }


            }

            @Override
            public void onFailure(Call<USERID> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "NO ID GET SOME PROBLEM", Toast.LENGTH_LONG).show();


            }
        });

    }
    public void GotoNextScreen() {
        int SPLASH_DISPLAY_LENGTH = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //if user is already logged in openeing the profile activity
                Intent mainIntent = new Intent(SplashScreen.this, OnBoarding.class);
                SplashScreen.this.startActivity(mainIntent);
                finish();


            }
        }, SPLASH_DISPLAY_LENGTH);


    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }


        return true;
    }







    }//end of Class


