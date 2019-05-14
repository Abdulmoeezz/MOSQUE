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
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentFirst extends Fragment {

    View v;
    TextView Sunrise,SehriEnds,FajarStart,FajarJammat,ZuharStart,ZuharJammat,AsrStart,AsrJammat,MagribStart,MagribJammat,IshaStart,IshaJammat;
    private ProgressDialog pDialog;
    String  SunRise,sehriends,Fstart,Fjamat,Zstart,Zjamat,Astart,AJammat,Mstart,Mjamat,Istart,Ijamat,restoredText;
    String PrimaryMosqueID,FavouriteMosqueID,Restoredtext,Currentdate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);











    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_first, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("CheckHours", Context.MODE_PRIVATE);
        restoredText = prefs.getString("hour", "24");


        Currentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        Restoredtext =    prefss.getString("PM_ID", null);

        SharedPreferences   FavPref = getActivity().getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE);
        FavouriteMosqueID = FavPref.getString("M_ID", null);


        if(FavouriteMosqueID == null){

                PrimaryMosqueID=Restoredtext;
                new GetJammatTimingsAMonth().execute();

        }else if(FavouriteMosqueID != null){

                PrimaryMosqueID=FavouriteMosqueID;
                new GetJammatTimingsAMonth().execute();
        }








     InitView();


        return v;
    }//End onCreateView Method



public void InitView(){


SehriEnds=v.findViewById(R.id.SehriEnds);
Sunrise=v.findViewById(R.id.txt_sunrise_time);

FajarStart=v.findViewById(R.id.txt_fajr_start_time);
FajarJammat=v.findViewById(R.id.txt_fajr_jamaat_time);

ZuharStart=v.findViewById(R.id.txt_zuhr_start_time);
ZuharJammat=v.findViewById(R.id.txt_zuhr_jamaat_time);

AsrStart=v.findViewById(R.id.txt_asr_start_time);
AsrJammat=v.findViewById(R.id.txt_asr_jamaat_time);

MagribStart=v.findViewById(R.id.txt_maghrib_start_time);
MagribJammat=v.findViewById(R.id.txt_maghrib_jamaat_time);


IshaStart=v.findViewById(R.id.txt_isha_start_time);
IshaJammat=v.findViewById(R.id.txt_isha_jamaat_time);


    }

    private class GetJammatTimingsAMonth extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Daily Timings ");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response


            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getMontlyPrayerTime/"+PrimaryMosqueID+"/"+Currentdate+"/D");
            Log.e("FT", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                   // JSONObject json = new JSONObject(jsonStr);

                    JSONArray friends = new JSONArray(jsonStr);
                    for (int index=0; index<friends.length(); index++){

                        JSONObject currentFriend = friends.getJSONObject(index);

                        if(restoredText.equals("24")){


                            SunRise = currentFriend.getString("Sunrise");
                            sehriends = currentFriend.getString("Sehri_ends");
                            Fstart = currentFriend.getString("Fajr");
                            Fjamat = currentFriend.getString("Jamaat1");
                            Zstart = currentFriend.getString("Zhuhr");
                            Zjamat = currentFriend.getString("Jamaat2");
                            Astart = currentFriend.getString("Asr");
                            AJammat = currentFriend.getString("Jamaat3");
                            Mstart = currentFriend.getString("Maghrib");
                            Mjamat = currentFriend.getString("Jamaat4");
                            Istart = currentFriend.getString("Isha");
                            Ijamat = currentFriend.getString("Jamaat5");






                        }else  if(restoredText.equals("12")){

                            try{

                                SunRise =TimeCHange( currentFriend.getString("Sunrise"));
                                sehriends = TimeCHange( currentFriend.getString("Sehri_ends"));
                                Fstart = TimeCHange( currentFriend.getString("Fajr"));
                                Fjamat = TimeCHange( currentFriend.getString("Jamaat1"));
                                Zstart = TimeCHange( currentFriend.getString("Zhuhr"));
                                Zjamat = TimeCHange( currentFriend.getString("Jamaat2"));
                                Astart = TimeCHange( currentFriend.getString("Asr"));
                                AJammat =TimeCHange(  currentFriend.getString("Jamaat3"));
                                Mstart = TimeCHange( currentFriend.getString("Maghrib"));
                                Mjamat = TimeCHange( currentFriend.getString("Jamaat4"));
                                Istart =TimeCHange(  currentFriend.getString("Isha"));
                                Ijamat = TimeCHange( currentFriend.getString("Jamaat5"));



                            }catch (ParseException ex){

                                ex.printStackTrace();

                            }







                        }







                    }







                    // SunRise = String.valueOf(json.getJSONObject("Sunrise"));
                    Log.d("okkk", "doInBackground: "+SunRise);





            } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Alert(getActivity());


                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
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
               Sunrise.setText(SunRise);
               SehriEnds.setText(sehriends);
               FajarStart.setText(Fstart);
               FajarJammat.setText(Fjamat);
               ZuharStart.setText(Zstart);
               ZuharJammat.setText(Zjamat);
               AsrStart.setText(Astart);
               AsrJammat.setText(AJammat);
               MagribStart.setText(Mstart);
               MagribJammat.setText(Mjamat);
               IshaStart.setText(Istart);
               IshaJammat.setText(Ijamat);

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
