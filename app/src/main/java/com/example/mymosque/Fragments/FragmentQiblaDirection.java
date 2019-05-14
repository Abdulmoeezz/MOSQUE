package com.example.mymosque.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.R;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.SENSOR_SERVICE;
import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;

public class FragmentQiblaDirection extends Fragment implements SensorEventListener {

    View v;
    //Variables  ImportStart
    private float compCurrentDegree=0f;
    private float qiblaCurrentDegree = 0f;
    Spinner lang ;
    private ImageView compImg,qiblaImg ;
    private SensorManager sensor;
    /* double latitude;
     double longitude;*/
    double lat,lng;
    double meccaLatitude = 21.422483;
    double meccaLongitude = 39.826181;
    float qiblaAngle;
    float compDegree,qiblaDegree;
    private LocationManager locationManager;
    private String provider;
    private TextView latituteField;
    private TextView longitudeField,EnglishDate,IslamicDate;
    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView textView;
    String lattitude,longitude;
    private String locationMode;

    //Variables  ImportStart










    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sensor = (SensorManager) Objects.requireNonNull(getActivity()).getSystemService(SENSOR_SERVICE);

        //Get Permission  From Applcation
      ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //Get Permission  From Applcation

    }//end of onCreate method

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_qibla_direction, container, false);






        SharedPreferences prefs = getActivity().getSharedPreferences("LatLang", Context.MODE_PRIVATE);
        lat = Double.parseDouble(prefs.getString("Lat", ""));
        lng = Double.parseDouble(prefs.getString("Lag", ""));


        Toast.makeText(getActivity(),lat+"  "+lng,Toast.LENGTH_SHORT).show();


        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Qibla Direction");
        //</For Toolbar>


        EnglishDate =v.findViewById(R.id.txt_qibla_day);
        IslamicDate=v.findViewById(R.id.txt_qibla_month);


        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String dates = df.format(Calendar.getInstance().getTime());
        EnglishDate.setText(dates);

        UmmalquraCalendar cal = new UmmalquraCalendar();
        cal.get(Calendar.YEAR);
        cal.get(Calendar.MONTH);
        cal.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH);

        cal.get(Calendar.DAY_OF_MONTH);
        String date=cal.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH)+"-"+cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.YEAR);
        IslamicDate.setText(date);

        //Initilize
        compImg = (ImageView)v.findViewById(R.id.compassImg);
        qiblaImg =(ImageView)v.findViewById(R.id.qiblaImg);
        //Initilize


        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            buildAlertMessageNoGps();

        } else {

           // getLocation();
            qiblaAngle = getQiblaAngle(lat,lng, meccaLatitude, meccaLongitude);


        }







        return v;
    }//End onCreateView Method






    @Override
    public void onSensorChanged(SensorEvent event) {

        compDegree = Math.round(event.values[0]);
        RotateAnimation compRotate = new RotateAnimation(compCurrentDegree ,- compDegree, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        compRotate.setDuration(210);
        compRotate.setFillAfter(true);
        compImg.startAnimation(compRotate);
        compCurrentDegree= - compDegree;

        qiblaDegree = Math.round(event.values[0])+qiblaAngle-90;
        RotateAnimation qiblaRotate = new RotateAnimation(qiblaCurrentDegree,-qiblaDegree,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        qiblaRotate.setDuration(210);
        qiblaRotate.setFillAfter(true);
        qiblaImg.startAnimation(qiblaRotate);
        qiblaCurrentDegree = -qiblaDegree;



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

/*

    @Override
    public void onStart() {
        super.onStart();

        sensor.registerListener((SensorEventListener) this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
        qiblaAngle = getQiblaAngle(lat,lng, meccaLatitude, meccaLongitude);

    }*/

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn on your GPS Connection please").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {

                try {
                   getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }catch (IllegalStateException ex){

                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.show();
    }


    public float getQiblaAngle(double lat1,double long1,double lat2,double long2){
        double angle,dy,dx;
        dy = lat2 - lat1;
        dx = Math.cos(Math.PI/ 180 * lat1) * (long2 - long1);
        angle = Math.atan2(dy, dx);
        angle = Math.toDegrees(angle);
        return (float)angle;
    }


    @Override
    public void onPause() {
        super.onPause();
        sensor.unregisterListener((SensorEventListener) this);
        qiblaAngle = getQiblaAngle(lat,lng, meccaLatitude, meccaLongitude);



    }


    @Override
    public void onResume() {
        super.onResume();
        sensor.registerListener((SensorEventListener) this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
        qiblaAngle = getQiblaAngle(lat,lng, meccaLatitude, meccaLongitude);




    }




















}//END CLASS
