package com.example.mymosque.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterNearestummah;
import com.example.mymosque.AdapterRVFavorite;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class ResultNearestJummah extends Fragment {


    View v;
    String UserID,PostalCode,City,Longitude,Lattitude,Jummah;
    String postcode;
    ArrayList<HashMap<String, String>> NearrestJummahList  = new ArrayList<>();
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    RelativeLayout  NotFoundLayout;
    TextView  TextViewCouldNot;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favorite, container, false);

        SharedPreferences UserPerfs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(UserPerfs.getInt("ID", 0));

        NotFoundLayout=v.findViewById(R.id.NotFoundLayout_);
        TextViewCouldNot=v.findViewById(R.id.textCouldnot_);





        try {

            SharedPreferences CurrentLocationPrefs = getActivity().getSharedPreferences("LatLang", Context.MODE_PRIVATE);
            Lattitude = CurrentLocationPrefs.getString("Lat", "0");
            Longitude = CurrentLocationPrefs.getString("Lag", "0");




            SharedPreferences MosqueData = getActivity().getSharedPreferences("PassNearestMosqueData", Context.MODE_PRIVATE);
            PostalCode=MosqueData.getString("PostalCode","");
            City=MosqueData.getString("City","");
            Jummah=MosqueData.getString("Jummah","");






        }catch (Exception ex){

            Toast.makeText(getActivity(),"We Are Unable to Get your Location Please check in Settings",Toast.LENGTH_SHORT).show();


        }


        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ImageView backbutton = (ImageView) toolbar.findViewById(R.id.backButton);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Result Nearest Jummah");
        //</For Toolbar>


      //  new FragmentFavorite.GetFavouriteList().execute();
          new GetNearestList().execute();



        return v;
    }//End onCreateView Method

    private class GetNearestList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Nearest Jummah List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();


            String replaced = (String) PostalCode.replace(" ","%20");

            // Making a request to url and getting response
            //+"/"+Jummah+"/"+City+"/"+PostalCode+"/"+Longitude+"/"+Lattitude

            String jsonStr ;

            if(PostalCode.equals("")){
                jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getNearestJummahcity/"+UserID+"/"+City+"");
            }else if(City.equals("")){
                jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getNearestJummahpostalcode/"+UserID+"/"+replaced+"");
            }else {
                 jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getNearestJummahcityandpostalcode/"+UserID+"/"+"true"+"/"+City+"/"+replaced+"");
            }




            SharedPreferences preferences = getActivity().getSharedPreferences("PostalCodePass", MODE_PRIVATE);
            preferences.edit().clear().apply();
            SharedPreferences preferencesss = getActivity().getSharedPreferences("CityPass", MODE_PRIVATE);
            preferencesss.edit().clear().apply();





            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("PassNearestMosqueData", MODE_PRIVATE).edit();
            editor.putString("PostalCode","");
            editor.putString("City","");
            editor.putString("Jummah","");
            editor.apply();

            Log.e(TAG, "Response from url: " + jsonStr);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray json = new JSONArray(jsonStr);


                    // looping through All Data
                    for (int i = 0; i < json.length(); i++) {



                        JSONObject e = json.getJSONObject(i);

                        /*map.put("id",  String.valueOf(i));
                        map.put("name", "Earthquake name:" + e.getString("eqid"));
                        map.put("magnitude", "Magnitude: " +  e.getString("magnitude"));
                        */

                        int id_ = e.getInt("J_id");
                        String name_ = e.getString("mosque_name");
                        String Jummah1_ = e.getString("Jummah1");
                        String Jummah2_ = e.getString("Jummah2");
                        String imageurl_ = e.getString("imageurl");
                        String timestamp = e.getString("timestamp");
                        String date_ = e.getString("date");
                        String  m_id= e.getString("m_id");
                        String address_=e.getString("address");
                        String longitude=e.getString("longtitude");
                        String lattitude=e.getString("latitude");



                        // Data node is JSON Array

                        // tmp hash map for single Data Array
                        HashMap<String, String> data_Hashmap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        data_Hashmap.put("id_", String.valueOf(id_));
                        data_Hashmap.put("name_", name_);
                        data_Hashmap.put("Jummah1_", Jummah1_);
                        data_Hashmap.put("Jummah2_", Jummah2_);
                        data_Hashmap.put("imageurl_", imageurl_);
                        data_Hashmap.put("timestamp", timestamp);
                        data_Hashmap.put("date_", date_);
                        data_Hashmap.put("address",address_);
                        data_Hashmap.put("m_id",m_id);
                        data_Hashmap.put("longitude",longitude);
                        data_Hashmap.put("lattitude",lattitude);


                            // adding data HashMap into list
                        NearrestJummahList.add(data_Hashmap);


                    }
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            NotFoundLayout.setVisibility(View.VISIBLE);
                            TextViewCouldNot.setText("Sorry we couldn't find any matches for Mosque");
                            Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        NotFoundLayout.setVisibility(View.VISIBLE);
                        TextViewCouldNot.setText("Sorry we couldn't find any matches for Mosque");


                     //   Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(NearrestJummahList.isEmpty()){

             //   Alert(getActivity());
                NotFoundLayout.setVisibility(View.VISIBLE);
                TextViewCouldNot.setText("Sorry we couldn't find any matches for Mosque");



            }else {


                recyclerView = (RecyclerView) v.findViewById(R.id.RV_FavoriteList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                ;
                recyclerView.setLayoutManager(layoutManager);

                //  Updating parsed JSON data into ListRecyclerView

                AdapterNearestummah adapter = new AdapterNearestummah(getActivity(),NearrestJummahList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }






        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();



    }


    @Override
    public void onStop() {
        super.onStop();



    }


}
