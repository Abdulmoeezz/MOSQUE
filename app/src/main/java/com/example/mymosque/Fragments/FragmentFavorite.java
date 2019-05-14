package com.example.mymosque.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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


import com.example.mymosque.AdapterRVFavorite;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;
import com.example.mymosque.RV_FindMasajid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentFavorite extends Fragment {

    View v;
    String UserID;
    ArrayList<HashMap<String, String>> FavouriteList  = new ArrayList<>();
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    RelativeLayout NotFoundLayout;
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



        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ImageView backbutton = (ImageView) toolbar.findViewById(R.id.backButton);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Favourite Mosques");
        //</For Toolbar>


       new GetFavouriteList().execute();




        return v;
    }//End onCreateView Method





    private class GetFavouriteList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Favorites List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/farvoriate/"+UserID);
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

                        int id_ = e.getInt("ID");
                        String name_ = e.getString("name");
                        String longitute_ = e.getString("longtitude");
                        String latitude_ = e.getString("latitude");
                        String imageurl_ = e.getString("imageurl");
                        int favourite_id_ = e.getInt("farvoriate_id");
                        int favourite_ = e.getInt("farvoriate");
                        String address_=e.getString("address");



                        // Data node is JSON Array

                        // tmp hash map for single Data Array
                        HashMap<String, String> data_Hashmap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        data_Hashmap.put("id_", String.valueOf(id_));
                        data_Hashmap.put("name_", name_);
                        data_Hashmap.put("longitute_", longitute_);
                        data_Hashmap.put("latitude_", latitude_);
                        data_Hashmap.put("imageurl_", imageurl_);
                        data_Hashmap.put("favourite_id_", String.valueOf(favourite_id_));
                        data_Hashmap.put("favourite_", String.valueOf(favourite_));
                        data_Hashmap.put("address",address_);

                        if(favourite_==1){
                            // adding data HashMap into list
                            FavouriteList.add(data_Hashmap);
                        }

                    }
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

                        NotFoundLayout.setVisibility(View.VISIBLE);
                        TextViewCouldNot.setText("Sorry,You Have No Favourite Mosque");

                        //Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
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

            if(FavouriteList.isEmpty()){

              // Alert(getActivity());
                NotFoundLayout.setVisibility(View.VISIBLE);
                TextViewCouldNot.setText("Sorry,You Have No Favourite Mosque");



            }else {


                recyclerView = (RecyclerView) v.findViewById(R.id.RV_FavoriteList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                recyclerView.setLayoutManager(layoutManager);

                //  Updating parsed JSON data into ListRecyclerView

                AdapterRVFavorite adapter = new AdapterRVFavorite(getActivity(), FavouriteList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }

    }

public void  Alert(Context context){

    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
    builder1.setMessage("Sorry You Don't do Any Favourite Mosques");
    builder1.setCancelable(true);

    builder1.setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentHome()).commit();


                }
            });

    builder1.setNegativeButton(
            "",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

    AlertDialog alert11 = builder1.create();
    alert11.show();







}


}//end of class
