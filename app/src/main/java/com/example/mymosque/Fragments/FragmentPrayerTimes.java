package com.example.mymosque.Fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.MyPagerAdapter;
import com.example.mymosque.AlarmReceiver;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.MainActivity;
import com.example.mymosque.R;
//import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import net.time4j.PlainDate;
import net.time4j.calendar.HijriAlgorithm;
import net.time4j.calendar.HijriCalendar;
import net.time4j.engine.StartOfDay;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentPrayerTimes extends Fragment {

    View v;
    Switch Switch;
   // String restoredText;
    Button ApplyAlaram,DisableAlaram;
    SharedPreferences.Editor EditPerivous;

    private  int mHour,mMin;
    String restoredText,Alramchecking,PrimaryMosQueID,Currentdate,RestoredAzanText,UserID,CheckNewSetPM, disablebutton;

    int[] hours=new int[5];
    int[] mins=new int[5];
    Calendar cal[];

    private PendingIntent pendingIntent;

    AlarmManager[] alarmManager = new AlarmManager[5];
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();


    private static final int ALARM_REQUEST_CODE = 133;
    ArrayList<HashMap<String, String>> NamazTimingsList  = new ArrayList<>();
    private ProgressDialog pDialog;
    Intent intent ;
    PendingIntent pi ;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(prefs.getInt("ID", 0));

        //Cecking HOurs TYpe
        SharedPreferences d = getActivity().getSharedPreferences("CheckHours", MODE_PRIVATE);
        restoredText   = d.getString("hour", null);

        //Cecking HOurs TYpe
        SharedPreferences AzanType = getActivity().getSharedPreferences("CheckAzan", MODE_PRIVATE);
        RestoredAzanText   = AzanType.getString("AzanType", null);


        //CAll Alram Pending Intent
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Checking Alram Off /ON
        SharedPreferences checkAlram = getActivity().getSharedPreferences("Switch", MODE_PRIVATE);
        Alramchecking   = checkAlram.getString("Switch", "off");

        //Get Primary Moaque ID
        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PrimaryMosQueID =    prefss.getString("PM_ID", null);


        SharedPreferences pref = getActivity().getSharedPreferences("GetPreviousMosque", MODE_PRIVATE);
        CheckNewSetPM = pref.getString("PreMosque_ID", "");


        SharedPreferences disablebuttonpref = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE);
        disablebutton= disablebuttonpref.getString("AlarmApply", "");



        EditPerivous = getActivity().getSharedPreferences("GetPreviousMosque", MODE_PRIVATE).edit();













    }//end of onCreate method

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_prayer_times, container, false);
        //<For Toolbar>
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Prayer Times");
        //</For Toolbar>







        ApplyAlaram =v.findViewById(R.id.BTN_Submit_);
        DisableAlaram=v.findViewById(R.id.BTN_Disable);


        Currentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("date", ""+Currentdate);
        SharedPreferences Date = getActivity().getSharedPreferences("DateAlaram", MODE_PRIVATE);





        if(CheckNewSetPM.equals("OK")){
            try {
                CancelAlramAll();
                ApplyAlaram.setVisibility(View.VISIBLE);
                DisableAlaram.setVisibility(View.GONE);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            EditPerivous.putString("PreMosque_ID","");
            EditPerivous.apply();

        }else {

            if(!Currentdate.equals(Date.getString("date",""))){

                ApplyAlaram.setVisibility(View.VISIBLE);
                DisableAlaram.setVisibility(View.GONE);

                try {
                    CancelAlramAll();
                }catch (NullPointerException ex){

                    ex.printStackTrace();

                }
            }else {
                ApplyAlaram.setVisibility(View.GONE);
                DisableAlaram.setVisibility(View.VISIBLE);

            }
        }










        TextView EnglishDate=v.findViewById(R.id.text_date);
        TextView IslamicDate=v.findViewById(R.id.IslamicDate);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String dates = df.format(Calendar.getInstance().getTime());
        EnglishDate.setText(dates);


        UmmalquraCalendar ca = new UmmalquraCalendar();
        ca.get(Calendar.YEAR);
        ca.get(Calendar.MONTH);
        ca.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH);
        ca.get(Calendar.DAY_OF_MONTH);


      //  Toast.makeText(this,""+cal,Toast.LENGTH_SHORT).show();
        Log.d("OK", "onCreate: "+ca.get(Calendar.YEAR)+"    "+ca.get(Calendar.MONTH)+"  "+ca.get(Calendar.DAY_OF_MONTH)+"   "+ca.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH));
        String date=ca.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH)+"-"+ca.get(Calendar.DAY_OF_MONTH)+"-"+ca.get(Calendar.YEAR);
        IslamicDate.setText(date);


        ApplyAlaram.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ApplyAlaram();
        DisableAlaram.setVisibility(View.VISIBLE);
        ApplyAlaram.setVisibility(View.GONE);

        Toast.makeText(getActivity(),"Alarm is Applying",Toast.LENGTH_SHORT).show();



        SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
        editoor.putString("AlarmApply","Yes");
        editoor.apply();






    }
});
        DisableAlaram.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View v) {

            try {
                CancelAlramAll();

        }catch (Exception ex){
            ex.printStackTrace();
        }
            ApplyAlaram.setVisibility(View.VISIBLE);
            DisableAlaram.setVisibility(View.GONE);

            SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
            editoor.putString("AlarmApply","No");
            editoor.apply();



    }
});

        final ViewPager pager = (ViewPager) v.findViewById(R.id.viewPager);
        ImageView leftNav = (ImageView) v.findViewById(R.id.left_nav);
        ImageView rightNav = (ImageView) v.findViewById(R.id.right_nav);
        // Images left navigation

        MyPagerAdapter  adapter =   new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       // pager.setOffscreenPageLimit(4);

                ViewPagerIndicator viewPagerIndicator = (ViewPagerIndicator) v.findViewById(R.id.view_pager_indicator);
                viewPagerIndicator.setupWithViewPager(pager);
                leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = pager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    pager.setCurrentItem(tab);
                } else if (tab == 0) {
                    pager.setCurrentItem(tab);
                }
            }
        });
                // Imageright navigatin
                rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = pager.getCurrentItem();
                tab++;
                pager.setCurrentItem(tab);
            }
        });


                if(disablebutton.equals("Yes")){
                    ApplyAlaram();
                }else
                {

                    Log.d("Result","Disable button is Not Visable");
                }



















        return  v;
    }//End onCreateView Method


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return new FragmentFirst();
                case 1: return new FragmentSecond();
                case 2: return new FragmentThird();
                case 3: return new FragmentFourth();

                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    public  void   ApplyAlaram(){

        SharedPreferences.Editor editoor = getActivity().getSharedPreferences("DateAlaram", MODE_PRIVATE).edit();
        editoor.putString("date",Currentdate);
        editoor.apply();


        new GetNamazTimingsAMonth().execute();


    }
    //Alaram Class  Getting Timings
    @SuppressLint("StaticFieldLeak")
    private class GetNamazTimingsAMonth extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
          //  pDialog.setMessage("Getting Daily Namaz Timing For  Applying Alarm");
          //  pDialog.setCancelable(false);
          //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/getMontlyPrayerTime/"+PrimaryMosQueID+"/"+Currentdate+"/B");
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
                           // Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                           // Alert(getActivity());
                            Log.d(TAG, "Alarm Is Not applying Beacuse ");


                        }
                    }); }//end of catch
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       //  Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                        //  Alert(getActivity());

                    }
                }); }//end of condition

            return null;
        }//onbackground

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
           // if (pDialog.isShowing())
             //   pDialog.dismiss();

            ArrayList<String> NAMAZTIME=new ArrayList<String>();

            for (int i =0;i<NamazTimingsList.size();i++){

                if(Currentdate.equals(NamazTimingsList.get(i).get("date_"))){
/*
                    SharedPreferences.Editor editoor = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
                    editoor.putString("AlarmApply","Yes");
                    editoor.apply();*/

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

                    //Function Alram is Calling
                    //triggerAlarmManager(NAMAZTIME);
                    //Function Alram is Calling
                   /* SharedPreferences Alram = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE);
                    if("Yes".equals(Alram.getString("AlarmApply", "Yes"))){
*/
                            triggerAlarmManager(NAMAZTIME);

                   // Toast.makeText(getActivity(),"Alarm is Applying From Function",Toast.LENGTH_SHORT).show();

/*


                            SharedPreferences.Editor editorAlram = getActivity().getSharedPreferences("ApplyAlaramOneTime", MODE_PRIVATE).edit();
                            editorAlram.putString("AlarmApply","NO");
                            editorAlram.apply();

                        }else {
                        Toast.makeText(getActivity(),"Today Date Namaz Reminder Already Set:",Toast.LENGTH_SHORT).show();
                        }

*/








                }

            }//end of loop

        }

    }




    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    public void triggerAlarmManager(ArrayList<String> list){



        try {
            for (int i = 0; i < list.size(); i++) {
                hours[i] = formaterHours(list.get(i));
                mins[i] = formaterMIns(list.get(i));
            }

        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }

      cal = new Calendar[5];

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


        }

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getActivity().getSharedPreferences("AzanAlaram", MODE_PRIVATE).edit();


     if   (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {


         for (int f = 0; f < cal.length; f++) {


             intent = new Intent(getActivity(), AlarmReceiver.class);


             pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);


             alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);


             alarmManager[f].set(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);


             intentArray.add(pi);


             // Toast.makeText(getActivity(), "FS:Azaan Is Set : "+ f +cal[f], Toast.LENGTH_SHORT).show();
             Log.d("alaram", String.valueOf(cal[f]));

         }



     }else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){

         for (int f = 0; f < cal.length; f++) {


             intent = new Intent(getActivity(), AlarmReceiver.class);


             pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);


             alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);


             alarmManager[f].setExact(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);


             intentArray.add(pi);


             // Toast.makeText(getActivity(), "FS:Azaan Is Set : "+ f +cal[f], Toast.LENGTH_SHORT).show();
             Log.d("alaram", String.valueOf(cal[f]));

         }





     }else {


         for (int f = 0; f < cal.length; f++) {


             intent = new Intent(getActivity(), AlarmReceiver.class);
             pi = PendingIntent.getBroadcast(getActivity(), f, intent, PendingIntent.FLAG_UPDATE_CURRENT);
             alarmManager[f] = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
             alarmManager[f].setExactAndAllowWhileIdle(Integer.parseInt(String.valueOf(AlarmManager.RTC_WAKEUP)), cal[f].getTimeInMillis(), pi);
             intentArray.add(pi);



             Log.d("alaram", String.valueOf(cal[f]));

         }


     }









    }
    public int formaterHours(String timefromedittext){


        SimpleDateFormat formatter  = new SimpleDateFormat("hh:mm");
        try {
            Date dTime = formatter.parse(String.valueOf(timefromedittext));

            mHour = dTime.getHours();

        }catch (Exception e)
        {

        }

        return mHour;

    }
    public int formaterMIns(String timefromedittext){


        SimpleDateFormat formatter  = new SimpleDateFormat("hh:mm");
        try {
            Date dTime = formatter.parse(String.valueOf(timefromedittext));


            mMin = dTime.getMinutes();
        }catch (Exception e)
        {

        }

        return mMin;

    }
    public void CancelAlramAll(){


        SharedPreferences.Editor editoor = getActivity().getSharedPreferences("DateAlaram", MODE_PRIVATE).edit();
        editoor.putString("date","kuchor");
        editoor.apply();


        for (int f = 0; f < cal.length; f++) {


            intent = new Intent(getActivity(), AlarmReceiver.class);
            pi = PendingIntent.getBroadcast(getActivity(), f, new Intent(getActivity(), AlarmReceiver.class), 0);
            alarmManager[f].cancel(pi);

            Toast.makeText(getActivity(),"Alaram All canceling",Toast.LENGTH_LONG).show();
            Log.d("alaram", String.valueOf(cal[f]));

        }


    }

}
