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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.AlarmReceiver;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.MainActivity;
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

public class FragmentSettings extends Fragment {


    View v;
    private RadioGroup radionGroup,AzanRadioGroup;
    RadioButton H24,H12,HFullAdan,HShortAdan;
    String restoredText,Alramchecking,PrimaryMosQueID,Currentdate,RestoredAzanText;

//Alaramm INITS
    private PendingIntent pendingIntent;
    private  int mHour,mMin;

    int[] hours=new int[5];
    int[] mins=new int[5];


     private static final int ALARM_REQUEST_CODE = 133;
    ArrayList<HashMap<String, String>> NamazTimingsList  = new ArrayList<>();
    private ProgressDialog pDialog;
    Intent intent ;
    PendingIntent pi ;
    android.widget.Switch Switch;

     //Alaramm INITS

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cecking HOurs TYpe
        SharedPreferences prefs = getActivity().getSharedPreferences("CheckHours", MODE_PRIVATE);
        restoredText   = prefs.getString("hour", "24");


        SharedPreferences AzanType = getActivity().getSharedPreferences("CheckAzan", MODE_PRIVATE);
        RestoredAzanText   = AzanType.getString("AzanType", "S");


         //CAll Alram Pending Intent
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Checking Alram Off /ON
        SharedPreferences checkAlram = getActivity().getSharedPreferences("AlaramCheck", MODE_PRIVATE);
        Alramchecking   = checkAlram.getString("Switch", "on");

         //Get Primary Moaque ID
        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PrimaryMosQueID =    prefss.getString("PM_ID", "");






    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_settings, container, false);


        Currentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Settings");
        //</For Toolbar>
      //  InitViews();


        H12=  v.findViewById(R.id.radioButton);
        H24=  v.findViewById(R.id.radioButton2);
        HFullAdan= v.findViewById(R.id.radioButtonFullAdhan);
        HShortAdan=  v.findViewById(R.id.radioButtonShortAdhan);


        try{

            if (RestoredAzanText.equals("F")) {

                HFullAdan.setChecked(true);

            } else if(RestoredAzanText.equals("S")){

                HShortAdan.setChecked(true);


            }



        }catch (Exception ex){


            ex.printStackTrace();
            HShortAdan.setChecked(true);

        }






        try {

            if (restoredText.equals("24")) {

                H24.setChecked(true);

            } else if(restoredText.equals("12")){

                H12.setChecked(true);


            }

        }catch ( Exception ex){


            H24.setChecked(true);

        }


        Switch=v.findViewById(R.id.Switch_Notification_);

        try {

            if (Alramchecking.equals("on")) {

                Switch.setChecked(true);
                Toast.makeText(getActivity(),"Alram is ON by You",Toast.LENGTH_SHORT).show();

            } else if(Alramchecking.equals("off")){

                Switch.setChecked(false);
                Toast.makeText(getActivity(),"Alram is OFF by You",Toast.LENGTH_SHORT).show();


            }

        }catch ( Exception ex){

            Switch.setChecked(true);

        }



        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("AlaramCheck", Context.MODE_PRIVATE).edit();

                if (isChecked) {
                    // The toggle is enabled
                    editor.putString("Switch","on");
                    editor.apply();
                    Switch.setChecked(true);
                    //Switch.setText("ON");
                    // Switch.setShowText(true);
                    Toast.makeText(getActivity(),"Alram is ON Now",Toast.LENGTH_SHORT).show();


                } else {

                    // The toggle is disabled
                    editor.putString("Switch","off");
                    editor.apply();
                    Switch.setChecked(false);
                    //    Switch.setText("OFF");
                    //  Switch.setShowText(true);
                    Toast.makeText(getActivity(),"Alram is OFF Now",Toast.LENGTH_SHORT).show();


                }
            }
        });
          radionGroup =  v.findViewById(R.id.radioGroup);
        AzanRadioGroup=  v.findViewById(R.id.radioGroupSalaah);
        InitButton();







        return v;
    }//End onCreateView Method

    public void  InitButton(){


     radionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
             RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
             SharedPreferences.Editor editor = getActivity().getSharedPreferences("CheckHours", MODE_PRIVATE).edit();



             if (checkedId== R.id.radioButton) {
                 Toast.makeText(getActivity(), "12  Hour Clock", Toast.LENGTH_SHORT).show();
                 editor.putString("hour","12");
                 editor.apply();
                 ReloadFragment();
                 H12.setChecked(true);



             }else if (checkedId==R.id.radioButton2){


                 Toast.makeText(getActivity(), "24 Hour Clock", Toast.LENGTH_SHORT).show();
                 editor.putString("hour","24");
                 editor.apply();
                 H24.setChecked(true);
                 ReloadFragment();


             }
















         }




     });
     AzanRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("CheckAzan", MODE_PRIVATE).edit();



                if (checkedId== R.id.radioButtonFullAdhan) {
                    Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    editor.putString("AzanType","F");
                    editor.apply();
                    ReloadFragment();
                    HFullAdan.setChecked(true);



                }else if (checkedId==R.id.radioButtonShortAdhan){


                    Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    editor.putString("AzanType","S");
                    editor.apply();
                    HShortAdan.setChecked(true);
                    ReloadFragment();


                }
















            }




        });








 }
    public void  ReloadFragment(){
        // ((MainActivity)getActivity()).refreshMyData();
        FragmentMyMosque fg = new FragmentMyMosque();
        getFragmentManager()  // or getSupportFragmentManager() if your fragment is part of support library
                .beginTransaction()
                .replace(R.id.Screen_Area, fg)
                .commit();


    }
    public  void   ApplyAlaram(){
        new GetNamazTimingsAMonth().execute();
    }




    //Alaram Class  Getting Timings
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
            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getMontlyPrayerTime/"+6+"/B");
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


                        // if(restoredText.equals("24")){
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





                    }//en of looop





                } //end of try
                catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Alert(getActivity());


                        }
                    }); }//end of catch
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();

                        Alert(getActivity());

                    }
                }); }//end of condition

            return null;
        }//onbackground

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ArrayList<String> NAMAZTIME=new ArrayList<String>();


            for (int i =0;i<NamazTimingsList.size();i++){

                if(Currentdate.equals(NamazTimingsList.get(i).get("date_"))){

                    SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
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

                    triggerAlarmManager(NAMAZTIME);
                    if(Alramchecking.equals("off")){


                        Log.e("Alram","Alram is not Apply");
                        Toast.makeText(getActivity(),"not Applying",Toast.LENGTH_SHORT).show();



                    }else {
                        SharedPreferences Alram = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE);


                        if("O".equals(Alram.getString("Alarm", null))){
                          //  triggerAlarmManager(NAMAZTIME);
                            Toast.makeText(getActivity(),"is Applying",Toast.LENGTH_SHORT).show();


                            SharedPreferences.Editor editorAlram = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
                            editorAlram.putString("Alarm","F");
                            editorAlram.apply();

                        }else {

                            //Thank you

                            Toast.makeText(getActivity(),"not AlramOneTime Applying",Toast.LENGTH_SHORT).show();
                        }


                    }






                }





            }//end of loop






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
               // Toast.makeText(getActivity(), "Hour && Mins"+i, Toast.LENGTH_SHORT).show();
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
            cal[i].set(Calendar.MILLISECOND,0);

            // cal[i].set(Calendar.AM_PM, 0);
            if(cal[i].before(Calendar.getInstance())) {
                cal[i].add(Calendar.DATE, 1);
            }

          //  Toast.makeText(getActivity(), "Ca"+i, Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("AzanAlaram", MODE_PRIVATE).edit();
        AlarmManager[] alarmManager = new AlarmManager[5];
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();



        for (int f = 0; f < cal.length; f++) {

/*
            if (f == 0) {
               *//* editor.putString("AzanFajar", "fajr");

                editor.apply();*//*

                intent = new Intent(getActivity(), AlarmReceiver.class);
                pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager[f].setExact(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);
                intentArray.add(pi);
                Toast.makeText(getActivity(), "AZAAN ALARAM IS SET", Toast.LENGTH_SHORT).show();


            } else {*/

               /* editor.putString("Azan", "normal");
                editor.apply();*/
            intent = new Intent(getActivity(), AlarmReceiver.class);
            pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager[f].set(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);
            intentArray.add(pi);
           // Toast.makeText(getActivity(), "FS:Azaan Is Set : "+ f +cal[f], Toast.LENGTH_SHORT).show();
            Log.d("alaram", String.valueOf(cal[f]));


            /*  }*/

        }

        //for fajar alaramm


        //for fajar alaramm

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















































}//classEnd
