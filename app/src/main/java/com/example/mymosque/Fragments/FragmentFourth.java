package com.example.mymosque.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterRVJamaatTimes;
import com.example.mymosque.Adapter.AdapterRVJummahTimes;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentFourth extends Fragment {


    ArrayList<HashMap<String, String>> JumaaHTimesList  = new ArrayList<>();
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    View v;
    String PrimaryMosqueID,Restoredtext,FavouriteMosqueID,restoredText,Currentdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fourth, container, false);
        v.findViewById(R.id.fourthfrag);


        SharedPreferences prefs = getActivity().getSharedPreferences("CheckHours", Context.MODE_PRIVATE);
        restoredText = prefs.getString("hour", "24");

        Currentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        Restoredtext =    prefss.getString("PM_ID", null);

        SharedPreferences   FavPref = getActivity().getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE);
        FavouriteMosqueID = FavPref.getString("M_ID", null);


        if(FavouriteMosqueID == null){

            PrimaryMosqueID=Restoredtext;
            JumaaHTimesList.clear();
            new GetJummahTimingsAMonth().execute();

        }else if(FavouriteMosqueID != null){

            PrimaryMosqueID=FavouriteMosqueID;
            JumaaHTimesList.clear();
            new GetJummahTimingsAMonth().execute();


        }
















        return v;
    }


    private class GetJummahTimingsAMonth extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your JAMAT Timings");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response


            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/Jummah/"+PrimaryMosqueID+"/"+Currentdate);
            Log.e("FT", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray json = new JSONArray(jsonStr);


                    // looping through All Data
                    for (int i = 0; i < json.length(); i++) {



                        JSONObject e = json.getJSONObject(i);

                        String date_ = e.getString("date");
                        String jummah1 = e.getString("Jummah1");
                        String jummah2 = e.getString("Jummah2");
                        int j_id = e.getInt("J_id");
                        int m_id = e.getInt("m_id");
                        String timestamp = e.getString("timestamp");
                        // Data node is JSON Array
                        // tmp hash map for single Data Array



                        if(restoredText.equals("24")){

                            HashMap<String, String> data_Hashmap = new HashMap<>();
                            // adding each child node to HashMap key => value
                            data_Hashmap.put("date_",date_);
                            data_Hashmap.put("jummah1",jummah1);
                            data_Hashmap.put("jummah2",jummah2);
                            data_Hashmap.put("timestamp",timestamp);
                            data_Hashmap.put("j_id", String.valueOf(j_id));
                            data_Hashmap.put("m_id", String.valueOf(m_id));
                            // adding data HashMap into list
                            JumaaHTimesList.add(data_Hashmap);





                        }else if(restoredText.equals("12")){


                            HashMap<String, String> data_Hashmap = new HashMap<>();
                            // adding each child node to HashMap key => value
                            data_Hashmap.put("date_",date_);
                            data_Hashmap.put("timestamp",timestamp);
                            data_Hashmap.put("j_id", String.valueOf(j_id));
                            data_Hashmap.put("m_id", String.valueOf(m_id));
                            // adding data HashMap into list

                            try {
                                data_Hashmap.put("jummah1",TimeCHange(jummah1));
                                data_Hashmap.put("jummah2",TimeCHange(jummah2));


                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            JumaaHTimesList.add(data_Hashmap);






                        }













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


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
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


            recyclerView = v.findViewById(R.id.RV_jummah_times);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);


            AdapterRVJummahTimes adapterRVJummahTimes = new AdapterRVJummahTimes(getActivity(),JumaaHTimesList);
            recyclerView.setAdapter(adapterRVJummahTimes);
            adapterRVJummahTimes.notifyDataSetChanged();




        }





    }

    public void  Alert(Context context){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("This is Mosques have No Timings Set by Admin");
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

    public  String TimeCHange(String Time) throws ParseException {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = displayFormat.parse(Time);
        return String.valueOf(parseFormat.format(date));



    }



}
