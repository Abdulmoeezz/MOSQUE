package com.example.mymosque.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterCity;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.MainActivity;
import com.example.mymosque.MapsActivity;
import com.example.mymosque.OnBoarding;
import com.example.mymosque.R;
import com.example.mymosque.SplashScreen;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;


import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class FragmentMyMosque extends Fragment {


    View v;
    ImageView   MosQueImage,AdvertiseImage;
    TextView    MosqueName,MosqueAddress,MosqueMiles;
    String      PrimaryMosQueID,mosquename,mosqueimageurl,mosqueAddress,mosquemiles,longitude,latitude,UserID;
    Double      CurrentLatitude,CurrentLongitude;
    Button      prayertimesBtn,mosqueBtn,ViewOpMap,GotoScreenMasajidlist;
    ArrayList<HashMap<String, String>> photosList  = new ArrayList<>();
    Runnable runnable ;
    RelativeLayout  DataPrimaryMosqueLayout,NoPrimaryMosqueLayout;
    int  i ;
    long timeadv = 5000;
    Double  rad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }//end of onCreate method

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_masjid, container, false);

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Primary Mosque");
        ImageView  Backbutton=toolbar.findViewById(R.id.backButton);
        AdvertiseImage=v.findViewById(R.id.Advertise_Image);
        //<For Toolbar>

        SharedPreferences prefs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(prefs.getInt("ID", 0));
        //</For Toolbar>
        DataPrimaryMosqueLayout=v.findViewById(R.id.MyPrimaryMosque);
        NoPrimaryMosqueLayout=v.findViewById(R.id.HideLayout);
        GotoScreenMasajidlist=v.findViewById(R.id.gotoNextScreen);

        NoPrimaryMosqueLayout.setVisibility(View.GONE);


        MosQueImage=v.findViewById(R.id.img_);
        MosqueName =v.findViewById(R.id.HeadingTxt1);
        MosqueAddress=v.findViewById(R.id.addressTxt2);
        MosqueMiles=v.findViewById(R.id.Txt_Miles);




        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PrimaryMosQueID = prefss.getString("PM_ID", "");
        mosquename=prefss.getString("PM_NAME", null);
        mosqueimageurl=prefss.getString("PM_URL",null);
        mosquemiles=prefss.getString("PM_Milesaway","");
        mosqueAddress=prefss.getString("PM_Address",null);
        longitude=prefss.getString("PM_longitude",null);
        latitude=prefss.getString("PM_latitude",null);



        try {
            SharedPreferences pre = getActivity().getSharedPreferences("LatLang", Context.MODE_PRIVATE);
            CurrentLatitude = Double.parseDouble(pre.getString("Lat", "0"));
            CurrentLongitude = Double.parseDouble(pre.getString("Lag", "0"));
        }catch (Exception ex){

            ex.printStackTrace();

        }

        if(CurrentLatitude == 0 || CurrentLongitude == 0){
            mosquemiles="Not Defined";
        }else {

            try {

                    LatLng CurrentLOcation= new LatLng(CurrentLatitude, CurrentLongitude);
                    LatLng DesLOcation= new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));


                    rad=computeDistanceBetween(CurrentLOcation,DesLOcation);
                    mosquemiles = String.format("%.2f",rad/1609.344);

              /*  mosquemiles = meterDistanceBetweenPoints(24.80016, 67.04518
                        , 31.582045, 74.329376);*/
            }catch (Exception ex){
                mosquemiles="Not Defined";
            }



        }




        if(PrimaryMosQueID==""){

       //  Alert(getContext());

            NoPrimaryMosqueLayout.setVisibility(View.VISIBLE);
            DataPrimaryMosqueLayout.setVisibility(View.GONE);

            GotoScreenMasajidlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentHome()).commit();


                }
            });



        }else {


            NoPrimaryMosqueLayout.setVisibility(View.GONE);
            DataPrimaryMosqueLayout.setVisibility(View.VISIBLE);

            MosqueName.setText(mosquename);
            MosqueAddress.setText(mosqueAddress);
            MosqueMiles.setText(mosquemiles);
           Picasso.get().load(mosqueimageurl).into(MosQueImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getActivity(),"ON Image Attach!!",Toast.LENGTH_SHORT).show();
                }


            });

        }


        prayertimesBtn = (Button) v.findViewById(R.id.PrayerTimesBTN);
        prayertimesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentAskImam()).commit(); }
        });

        mosqueBtn = (Button) v.findViewById(R.id.MosqueBTN);
        mosqueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentPrayerTimes()).commit();
            }
        });

        ViewOpMap = (Button) v.findViewById(R.id.VIewonMapBTN);
        ViewOpMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Location", MODE_PRIVATE).edit();
                editor.putString("Latitude",latitude);
                editor.putString("Longitude", longitude);
                editor.apply();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent); }
        });

                    new GettingPhotos().execute();
                    advertise();


                    return v;
    }//End onCreateView Method



    public void  Alert(Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Sorry You Don't do Any Primary Mosque");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentHome()).commit();


                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    @SuppressLint("StaticFieldLeak")
    private class GettingPhotos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response


            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/adverts/"+UserID);
            Log.e("FT", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray json = new JSONArray(jsonStr);


                    // looping through All Data
                    for (int i = 0; i < json.length(); i++) {



                        JSONObject e = json.getJSONObject(i);

                        int ads_id = e.getInt("ads_id");
                        String duration=e.getString("duration");
                        String file_name=e.getString("file_name");
                        String file_path=e.getString("file_path");
                        String show_time=e.getString("show_time");
                        String m_id=e.getString("m_id");
                        String is_active=e.getString("is_active");
                        String timestamp=e.getString("timestamp");
                        String start_date=e.getString("start_date");
                        String end_date=e.getString("end_date");


                       // tmp hash map for single Data Array
                        HashMap<String, String> data_Hashmap = new HashMap<>();
                        // adding each child node to HashMap key => value
                        data_Hashmap.put("ads_id", String.valueOf(ads_id));
                        data_Hashmap.put("duration", duration);
                        data_Hashmap.put("file_name", file_name);
                        data_Hashmap.put("file_path", file_path);
                        data_Hashmap.put("show_time",show_time);
                        data_Hashmap.put("m_id", m_id);
                        data_Hashmap.put("is_active", is_active);
                        data_Hashmap.put("timestamp", timestamp);
                        data_Hashmap.put("start_date", start_date);
                        data_Hashmap.put("end_date", end_date);
                        photosList.add(data_Hashmap);

                    }
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                          //  Alert(getActivity());


                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                     //   Alert(getActivity());



                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            advertise();



        }
    }
    public void  advertise(){
        runnable = new Runnable() {
                @Override
                public void run() {
                 for (i=0;i<photosList.size();i++){

                        String path="http://masjidi.co.uk/"+photosList.get(i).get("file_path")+photosList.get(i).get("file_name");
                   // Toast.makeText(getActivity(),"!!!!!!"+i,Toast.LENGTH_LONG).show();
                     Log.d("advertise", ""+path);
                     Picasso.get().load(path).into(AdvertiseImage, new com.squareup.picasso.Callback() {
                         @Override
                         public void onSuccess() {


                         }

                         @Override
                         public void onError(Exception e) {
                             Toast.makeText(getActivity(), "No Map Found !!", Toast.LENGTH_SHORT).show();
                         }


                     });
                    // handler.postDelayed(runnable, Long.parseLong(Objects.requireNonNull(photosList.get(i).get("duration")))*1000);
                    // our runnable should keep running for every 1000 milliseconds (1 seconds)
                        timeadv = Long.parseLong(Objects.requireNonNull(photosList.get(i).get("duration")))*1000 ;
                   }
                }
            };

        Handler handler = new Handler();
            handler.postDelayed(runnable, timeadv);
            runnable.run();





}




}//end of class
