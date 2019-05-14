package com.example.mymosque.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.OnBottomReachedListener;
import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.MainActivity;
import com.example.mymosque.Model.Favourite;
import com.example.mymosque.Model.MasajidList;
import com.example.mymosque.Model.Masjid;
import com.example.mymosque.Model.MosqueData;
import com.example.mymosque.R;
import com.example.mymosque.RV_FindMasajid;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static android.support.v4.content.ContextCompat.getSystemService;
import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class FragmentHome extends Fragment{


    View v;
    ImageView Humbburger;
    EditText search_masjid;
    DrawerLayout mDrawerLayout;
    RecyclerView recyclerView;
    double longitude;
    double latitude;
    private ProgressDialog pDialog;
    String UserID;
    String jsonStr;
    String Next_;
    double CurrentLatitude,CurrentLongitude;
    RV_FindMasajid adapter ;
    private  ArrayList<MosqueData> allMosques ;
    MosqueData mosqueData ;
    RelativeLayout  RecyclerViewLayout, NoInternetLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*   if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getActivity(),"google play servives is not available",Toast.LENGTH_SHORT).show();
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        */
        SharedPreferences prefs = getActivity().getSharedPreferences("LatLang", Context.MODE_PRIVATE);
        CurrentLatitude = Double.parseDouble(prefs.getString("Lat", "0"));
        CurrentLongitude = Double.parseDouble(prefs.getString("Lag", "0"));




    }//end of onCreate method


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmenthome, container, false);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //When Fragment is Open this SharedPref Must have to use itt .
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("LatLang", Context.MODE_PRIVATE).edit();
        editor.putString("Lat", String.valueOf(latitude));
        editor.putString("Lag", String.valueOf(longitude));
        editor.apply();


        SharedPreferences prefs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(prefs.getInt("ID", 0));


        ImageView  Humnburger=v.findViewById(R.id.humburgerIcon);
        Humnburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).GetDrawer();

            }
        });

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        //</For Toolbar>

        recyclerView = (RecyclerView) v.findViewById(R.id.RV_masajidList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if(!Next_.equals("null")) {
                        new GetUpdateList().execute();
                    }else{ Toast.makeText(getActivity(),"Masajid List Is End", Toast.LENGTH_SHORT).show(); }
                }
            }
        });

        //Init  Arraylist();
        allMosques = new ArrayList<>();

        //View  Init
        search_masjid = (EditText) v.findViewById(R.id.edit_txt_masjid);
        Humbburger = (ImageView) v.findViewById(R.id.humburgerIcon);
        mDrawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);


        search_masjid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (adapter != null) {
                    filter(editable.toString());
                }
            }
        });



        RecyclerViewLayout=v.findViewById(R.id.ListLayout_);
        NoInternetLayout=v.findViewById(R.id.NotFoundLayout_);






        //Function Calling
       new GetContacts().execute();
        //Function Calling








        return v;
    }//End onCreateView Method



  /*  @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

      //  Toast.makeText(getActivity()," FH LONGITUDE LATITUDE"+latitude+" @@ "+ longitude,Toast.LENGTH_SHORT).show();






    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
*/


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting  Masajid List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getMosquesList/"+UserID);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray DATA_ = jsonObj.getJSONArray("data");



                    JSONObject LINKS = jsonObj.getJSONObject("links");



                    String First_ = LINKS.getString("first");
                    String Last_ = LINKS.getString("last");
                    Next_ = LINKS.getString("next");


                    Log.e("Data", "doInBackground: data [] "+DATA_);
                    Log.e("Links", "doInBackground: links {} "+LINKS);
                    Log.e("First", "doInBackground: links:> first "+First_);
                    Log.e("Last", "doInBackground: links:> last "+Last_);
                    Log.e("Next", "doInBackground: links:> next "+Next_);




                    // looping through All Data
                    for (int i = 0; i < DATA_.length(); i++) {

                        mosqueData = new MosqueData();
                        JSONObject getmosquedata = DATA_.getJSONObject(i);



                        mosqueData.setID(getmosquedata.getInt("ID"));
                        mosqueData.setName(getmosquedata.getString("name"));
                        mosqueData.setLongtitude(getmosquedata.getString("longtitude"));
                        mosqueData.setLatitude(getmosquedata.getString("latitude"));
                        mosqueData.setImageurl(getmosquedata.getString("imageurl"));
                        mosqueData.setFarvoriate_id(getmosquedata.getInt("farvoriate_id"));
                        mosqueData.setFarvoriate(getmosquedata.getInt("farvoriate"));
                        mosqueData.setAddress(getmosquedata.getString("address"));
                        LatLng CurreLatlng=new LatLng(CurrentLatitude,CurrentLongitude);
                        // LatLng DesLatlng=new LatLng(DestinationLatitude,DestinationLongitude);
                      LatLng DesLatlng=new LatLng(Double.parseDouble(getmosquedata.getString("latitude")),Double.parseDouble(getmosquedata.getString("longtitude")));
                      Double Miles =computeDistanceBetween(CurreLatlng,DesLatlng);
                      mosqueData.setMiles(Miles/1609.344);
                        allMosques.add(mosqueData);
                        // Data node is JSON Array

                    }
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                         //   Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        NoInternetLayout.setVisibility(View.VISIBLE);
                        RecyclerViewLayout.setVisibility(View.INVISIBLE);

                     //   Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();



                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
                adapter = new RV_FindMasajid(getActivity(),allMosques,UserID);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }





        }

    }







  /*  private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }*/


    private void filter(String text) {
        ArrayList<MosqueData> filteredList = new ArrayList<>();

        for (MosqueData item : allMosques) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }




    private class GetUpdateList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*  pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting  Masajid List");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();



            if(Next_ != null){
                jsonStr = sh.makeServiceCall(Next_);
                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(jsonStr);


                        // Getting JSON Array node
                        JSONArray DATA_ = jsonObj.getJSONArray("data");


                        // looping through All Data
                        for (int i = 0; i < DATA_.length(); i++) {

                            mosqueData = new MosqueData();
                            JSONObject getmosquedata = DATA_.getJSONObject(i);



                            mosqueData.setID(getmosquedata.getInt("ID"));
                            mosqueData.setName(getmosquedata.getString("name"));
                            mosqueData.setLongtitude(getmosquedata.getString("longtitude"));
                            mosqueData.setLatitude(getmosquedata.getString("latitude"));
                            mosqueData.setImageurl(getmosquedata.getString("imageurl"));
                            mosqueData.setFarvoriate_id(getmosquedata.getInt("farvoriate_id"));
                            mosqueData.setFarvoriate(getmosquedata.getInt("farvoriate"));
                            mosqueData.setAddress(getmosquedata.getString("address"));
                            LatLng CurreLatlng=new LatLng(CurrentLatitude,CurrentLongitude);
                            // LatLng DesLatlng=new LatLng(DestinationLatitude,DestinationLongitude);
                            LatLng DesLatlng=new LatLng(Double.parseDouble(getmosquedata.getString("latitude")),Double.parseDouble(getmosquedata.getString("longtitude")));
                            Double Miles =computeDistanceBetween(CurreLatlng,DesLatlng);
                            mosqueData.setMiles(Miles/1609.344);
                            allMosques.add(mosqueData);
                            // Data node is JSON Array

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            }
                        });



                        JSONObject LINKS = jsonObj.getJSONObject("links");



                        String First_ = LINKS.getString("first");
                        String Last_ = LINKS.getString("last");
                        Next_ = LINKS.getString("next");


                        Log.e("Data", "doInBackground: data [] "+DATA_);
                        Log.e("Links", "doInBackground: links {} "+LINKS);
                        Log.e("First", "doInBackground: links:> first "+First_);
                        Log.e("Last", "doInBackground: links:> last "+Last_);
                        Log.e("Next", "doInBackground: links:> next "+Next_);





                    } catch (final JSONException e) {
                        Log.e("ok", "Json parsing error: " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //  Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
         /*   if(pDialog.isShowing()){
                pDialog.dismiss();

            }*/

        }

    }





}
