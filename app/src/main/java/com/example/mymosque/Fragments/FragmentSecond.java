package com.example.mymosque.Fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterRVTimes;
import com.example.mymosque.AlarmReceiver;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentSecond extends Fragment {

    ArrayList<HashMap<String, String>> NamazTimingsList  = new ArrayList<>();
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    View v;
    String Currentdate;
    private PendingIntent pendingIntent;
    private  int mHour,mMin;

    int[] hours=new int[5];
    int[] mins=new int[5];
    private static final int ALARM_REQUEST_CODE = 133;
    String PrimaryMosqueID,Restoredtext,FavouriteMosqueID;
    String restoredText,Alramchecking;

    Intent intent ;

    PendingIntent pi ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          v = inflater.inflate(R.layout.fragment_second, container, false);




        Currentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences prefs = getActivity().getSharedPreferences("CheckHours", Context.MODE_PRIVATE);
        restoredText = prefs.getString("hour", "24");






        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        SharedPreferences checkAlram = getActivity().getSharedPreferences("Switch", Context.MODE_PRIVATE);
        Alramchecking   = checkAlram.getString("Switch", "off");


        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        Restoredtext =    prefss.getString("PM_ID", null);

        SharedPreferences   FavPref = getActivity().getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE);
        FavouriteMosqueID = FavPref.getString("M_ID", null);


        if(FavouriteMosqueID == null){

            PrimaryMosqueID=Restoredtext;
            NamazTimingsList.clear();
            new GetNamazTimingsAMonth().execute();

        }else if(FavouriteMosqueID != null){

            PrimaryMosqueID=FavouriteMosqueID;
            NamazTimingsList.clear();
            new GetNamazTimingsAMonth().execute();


        }





        return v;
    }

    private class GetNamazTimingsAMonth extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Namaz Timings ");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response


            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getMontlyPrayerTime/"+PrimaryMosqueID+"/"+Currentdate+"/B");
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray json = new JSONArray(jsonStr);

                    // looping through All Data
                    for (int i = 0; i < json.length(); i++) {



                        JSONObject e = json.getJSONObject(i);

                        String Date_ = e.getString("Date");
                        String Fajr_ = e.getString("Fajr");
                        String Zhuhr_ = e.getString("Zhuhr");
                        String Asr_ = e.getString("Asr");
                        String Maghrib_ = e.getString("Maghrib");
                        String Isha_ = e.getString("Isha");
                        // Data node is JSON Array
                        // tmp hash map for single Data Array



                        if(restoredText.equals("24")){
                            HashMap<String, String> data_Hashmap = new HashMap<>();
                            // adding each child node to HashMap key => value
                            data_Hashmap.put("date_", Date_);
                            data_Hashmap.put("fajr_", Fajr_);
                            data_Hashmap.put("zhuhr_", Zhuhr_);
                            data_Hashmap.put("asr__", Asr_);
                            data_Hashmap.put("maghrib_", Maghrib_);
                            data_Hashmap.put("isha_", Isha_);
                            // adding data HashMap into list
                            NamazTimingsList.add(data_Hashmap);


                        }else if(restoredText.equals("12")){

                            HashMap<String, String> data_Hashmap = new HashMap<>();
                            // adding each child node to HashMap key => value
                            data_Hashmap.put("date_",Date_);
                            try {
                                data_Hashmap.put("fajr_",TimeCHange(Fajr_));
                                data_Hashmap.put("zhuhr_", TimeCHange(Zhuhr_));
                                data_Hashmap.put("asr__",TimeCHange(Asr_));
                                data_Hashmap.put("maghrib_",TimeCHange(Maghrib_));
                                data_Hashmap.put("isha_",TimeCHange(Isha_));

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            NamazTimingsList.add(data_Hashmap);

                            // adding data HashMap into list






                        }
                    }

                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         //   Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                           // Alert(getActivity());


                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();

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
            if (pDialog.isShowing())
                pDialog.dismiss();


            recyclerView =(RecyclerView) v.findViewById(R.id.RV_times);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);

            AdapterRVTimes adapterRVTimes = new AdapterRVTimes(getActivity(),NamazTimingsList);
            recyclerView.setAdapter(adapterRVTimes);
            adapterRVTimes.notifyDataSetChanged();


      /*      ArrayList<String>  NAMAZTIME=new ArrayList<String>();


            for (int i =0;i<NamazTimingsList.size();i++){

                if(Currentdate.equals(NamazTimingsList.get(i).get("date_"))){

                    SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", Context.MODE_PRIVATE).edit();
                    editoor.putString("Alarm","O");
                    editoor.apply();



                    String F =NamazTimingsList.get(i).get("fajr_");
                    String Z=NamazTimingsList.get(i).get("zhuhr_");
                    String A =NamazTimingsList.get(i).get("asr__");
                    String M =NamazTimingsList.get(i).get("maghrib_");
                    String I =NamazTimingsList.get(i).get("isha_");
                    NAMAZTIME.add(F);
                    NAMAZTIME.add(Z);
                    NAMAZTIME.add(A);
                    NAMAZTIME.add(M);
                    NAMAZTIME.add(I);
                    if(Alramchecking.equals("off")){


                        Log.e("Alram","Alram is not Apply");
                        Toast.makeText(getActivity(),"not Applying",Toast.LENGTH_SHORT).show();



                    }else {
                        SharedPreferences Alram = getActivity().getSharedPreferences("ApplyAlaramOneTime", Context.MODE_PRIVATE);


                        if("O".equals(Alram.getString("Alarm", null))){
                          //  triggerAlarmManager(NAMAZTIME);
                            Toast.makeText(getActivity(),"is Applying",Toast.LENGTH_SHORT).show();


                            SharedPreferences.Editor editorAlram = getActivity().getSharedPreferences("ApplyAlaramOneTime", Context.MODE_PRIVATE).edit();
                            editorAlram.putString("Alarm","F");
                            editorAlram.apply();

                        }else {

                            //Thank you

                            Toast.makeText(getActivity(),"not AlramOneTime Applying",Toast.LENGTH_SHORT).show();
                        }


                    }






                }





            }//end of loop
*/





        }

    }

    public void triggerAlarmManager(ArrayList<String> list){

      /*  SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", Context.MODE_PRIVATE).edit();
        editoor.putString("Alarm","O");
        editoor.apply();*/

try {
    for (int i = 0; i < list.size(); i++) {
        hours[i] = formaterHours(list.get(i));
        mins[i] = formaterMIns(list.get(i));
    }

}catch (IndexOutOfBoundsException ex){
    ex.printStackTrace();
}

        Calendar cal[] = new Calendar[5];

        for (int i = 0; i < 5; i++) {
            cal[i] = Calendar.getInstance();
            cal[i].set(Calendar.HOUR_OF_DAY, hours[i]);
            cal[i].set(Calendar.MINUTE, mins[i]);
            cal[i].set(Calendar.SECOND, 0);
           // cal[i].set(Calendar.AM_PM, 0);
        }
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("AzanAlaram", Context.MODE_PRIVATE).edit();



        AlarmManager[] alarmManager = new AlarmManager[5];
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();



        for (int f = 0; f < cal.length; f++) {


            if (f == 0) {
                editor.putString("AzanFajar", "fajr");

                editor.apply();

                intent = new Intent(getActivity(), AlarmReceiver.class);
                pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager[f].set(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);
                intentArray.add(pi);
                Toast.makeText(getActivity(), "AZAAN ALARAM IS SET", Toast.LENGTH_SHORT).show();


            } else {

                editor.putString("Azan", "normal");
                editor.apply();
                intent = new Intent(getActivity(), AlarmReceiver.class);
                pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager[f].set(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);
                intentArray.add(pi);
                Toast.makeText(getActivity(), "AZAAN ALARAM IS SET", Toast.LENGTH_SHORT).show();


            }

        }




    }


    public int formaterHours(String timefromedittext){


        SimpleDateFormat formatter  = new SimpleDateFormat("hh:mm");
        try {
            Date dTime = formatter.parse(String.valueOf(timefromedittext));

           mHour = dTime.getHours();
          //  mMin = dTime.getMinutes();
        }catch (Exception e)
        {

        }

        return mHour;

    }
    public int formaterMIns(String timefromedittext){


        SimpleDateFormat formatter  = new SimpleDateFormat("hh:mm");
        try {
            Date dTime = formatter.parse(String.valueOf(timefromedittext));

          //  mHour = dTime.getHours();
            mMin = dTime.getMinutes();
        }catch (Exception e)
        {

        }

        return mMin;

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
