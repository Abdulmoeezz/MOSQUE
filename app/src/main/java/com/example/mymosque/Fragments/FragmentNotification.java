package com.example.mymosque.Fragments;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterNotificationRV;
import com.example.mymosque.AdapterRVFavorite;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentNotification extends Fragment {

    View v;
    String UserID;
    RelativeLayout Layout;


    ArrayList<HashMap<String, String>> NotificationList  = new ArrayList<>();
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        Layout =v.findViewById(R.id.Notification_layout_);
        Layout.setVisibility(View.GONE);


        SharedPreferences UserPerfs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(UserPerfs.getInt("ID", 0));

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Notifications");
        //</For Toolbar>


new GetNotifications().execute();






        return v;
    }//End onCreateView Method





    private class GetNotifications extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Notification List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getNotification/"+UserID);
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

                        int N_ID = e.getInt("n_id");
                        int m_ID = e.getInt("m_id");
                        String FileName = e.getString("file_name");
                        String FilePath = e.getString("file_path");
                        String Type = e.getString("type");
                        String Description = e.getString("discription");
                        String Time = e.getString("timestamp");




                        // Data node is JSON Array

                        // tmp hash map for single Data Array
                        HashMap<String, String> data_Hashmap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        data_Hashmap.put("N_id", String.valueOf(N_ID));
                        data_Hashmap.put("M_id", String.valueOf(m_ID));
                        data_Hashmap.put("Filename_", FileName);
                        data_Hashmap.put("Filepath_", FilePath);
                        data_Hashmap.put("Type_", Type);
                        data_Hashmap.put("Description_", Description);
                        data_Hashmap.put("Time_", Time);
                        NotificationList.add(data_Hashmap);


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
                      //  Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                        HideLayout();


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

            if(NotificationList.isEmpty()){

              //  Alert(getActivity());
                HideLayout();



            }else {


                recyclerView = (RecyclerView) v.findViewById(R.id.Notifications_RV);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                ;
                recyclerView.setLayoutManager(layoutManager);

                //  Updating parsed JSON data into ListRecyclerView

                AdapterNotificationRV adapter = new AdapterNotificationRV(getActivity(), NotificationList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }

    }







public  void  HideLayout(){

    Layout.setVisibility(View.VISIBLE);






}










}
