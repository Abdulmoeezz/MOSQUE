package com.example.mymosque;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Fragments.FragmentAddMosque;
import com.example.mymosque.Fragments.FragmentDua;
import com.example.mymosque.Fragments.FragmentFavorite;
import com.example.mymosque.Fragments.FragmentFeedback;
import com.example.mymosque.Fragments.FragmentHome;
import com.example.mymosque.Fragments.FragmentMyMosque;
import com.example.mymosque.Fragments.FragmentNearestJummah;
import com.example.mymosque.Fragments.FragmentNotification;
import com.example.mymosque.Fragments.FragmentQiblaDirection;
import com.example.mymosque.Fragments.FragmentSettings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{

    ImageView Humbburger, backbutton;
    DrawerLayout mDrawerLayout;
    TextView shareapp;
    String PrimaryMosQueID;

    //Location Golobal Variables
    Location mLocation;
    TextView latLng;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 9000;  /* 2 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notification Working From FireBAse with Admin Penel
        FirebaseMessaging.getInstance().subscribeToTopic("Testing").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        });
        //Getting Primary MOsque ID for Checking;
        SharedPreferences SharedUserID = getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PrimaryMosQueID = SharedUserID.getString("PM_ID", "");


        //When everyTIme  Main Activity Start Remove  View From FRameLayout
        FrameLayout F1 = findViewById(R.id.Screen_Area);
        F1.removeAllViews();

        //CHecking Which Fragment Open First when Primary MosQue Assign Or not
        if (PrimaryMosQueID.equals("")){

            F1.removeAllViews();
            Fragment HomeF= new FragmentHome();
            getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, HomeF).addToBackStack(null).commit();


        } else {

            F1.removeAllViews();
            FragmentMyMosque myMosQueFragment = new FragmentMyMosque();
            getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, myMosQueFragment).addToBackStack(null).commit();
        }
        //share App Button Call
        shareapp = (TextView) findViewById(R.id.text12);
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Mosque");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));


                } catch (Exception e) {
                    //e.toString();
                }

            }


       });
        //Button Calls Navigation view
        backbutton = (ImageView) findViewById(R.id.backButton);
        Humbburger = (ImageView) findViewById(R.id.humburgerIcon);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Humbburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmenttttt = getSupportFragmentManager().findFragmentById(R.id.Screen_Area);
                if(fragmenttttt instanceof FragmentHome) {
                    finish();
                }else if(fragmenttttt instanceof  FavoriteProfileFragment){


                    FragmentFavorite fragment = new FragmentFavorite();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.Screen_Area, fragment, fragment.getTag()).commit();

                } else{



                    FragmentHome fragment = new FragmentHome();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.Screen_Area, fragment, fragment.getTag()).commit();

                    SharedPreferences.Editor editor = getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE).edit();
                    editor.putString("M_ID", null);
                    editor.putString("M_name", null);
                    editor.putString("M_Image_", null);
                    editor.apply();
                    count++;
                }
            }


        });
        InitNavigationVIew();
        //LOcation  Working Works


       // latLng = (TextView) findViewById(R.id.latLng);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0){

                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);


            }
        }



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



    }//End of OnCreate Method//


        public void GetDrawer(){

        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }
         public void InitNavigationVIew(){

        TextView home_ = (TextView) findViewById(R.id.text1);
        home_.setOnClickListener(this);


        TextView Feedback_ = (TextView) findViewById(R.id.text11);
        Feedback_.setOnClickListener(this);


        TextView Dua_ = (TextView) findViewById(R.id.text9);
        Dua_.setOnClickListener(this);


        TextView fragmentmassajilist_ = (TextView) findViewById(R.id.text3);
        fragmentmassajilist_.setOnClickListener(this);


       /* TextView fragmentnearestmassajid_ = (TextView) findViewById(R.id.text4);
        fragmentnearestmassajid_.setOnClickListener(this);*/


        TextView fragmentnearestjummah_ = (TextView) findViewById(R.id.text5);
        fragmentnearestjummah_.setOnClickListener(this);


        TextView fragmentsettings_ = (TextView) findViewById(R.id.text10);
        fragmentsettings_.setOnClickListener(this);


        TextView fragmentnotification_ = (TextView) findViewById(R.id.text7);
        fragmentnotification_.setOnClickListener(this);


        TextView fragmentaddmosque_ = (TextView) findViewById(R.id.text8);
        fragmentaddmosque_.setOnClickListener(this);


        TextView fragqibla = (TextView) findViewById(R.id.text6);
        fragqibla.setOnClickListener(this);


        TextView fragmentfavorite = (TextView) findViewById(R.id.text2);
        fragmentfavorite.setOnClickListener(this);






    }


        @Override
        protected void onResume() {
        super.onResume();

            if (!checkPlayServices()) {
                latLng.setText("Please install Google Play services.");
            }


    }

         @Override
            protected void onStart() {
                super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

        @Override
        protected void onPause() {
        super.onPause();


    }






        private boolean doubleBackToExitPressedOnce = false;
        @Override
        public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                showHome();
        }
    }
         //Apply Call Alaram from PyarTimes Fragment to Settings GetTimeLIst Method



        public  void  ApplyAlaramMain(){

        FragmentSettings ff=new FragmentSettings();
        ff.ApplyAlaram();
        }

        private void showHome() {
        Fragment fragmenttttt = getSupportFragmentManager().findFragmentById(R.id.Screen_Area);
            if(fragmenttttt instanceof FragmentHome) {
                finish();



            }else if(fragmenttttt instanceof  FavoriteProfileFragment){


                FragmentFavorite fragment = new FragmentFavorite();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.Screen_Area, fragment, fragment.getTag()).commit();



            } else{



                FragmentHome fragment = new FragmentHome();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.Screen_Area, fragment, fragment.getTag()).commit();

                SharedPreferences.Editor editor = getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE).edit();
                editor.putString("M_ID", null);
                editor.putString("M_name", null);
                editor.putString("M_Image_", null);
                editor.apply();
                count++;
            }



        }

        @Override
        public void onClick(View v) {
        Fragment fragment = null;
        Class fragmentClass = null;

        if (v.getId() == R.id.text1) {
            //do something here if button1 is clicked

            fragmentClass = FragmentMyMosque.class;
        } /*else if (v.getId() == R.id.text4) {
            fragmentClass = FragmentNearestMasajid.class;
            //do something here if button3 is clicked
            // fragmentClass = FragmentLevel.class;
        }*/ else if (v.getId() == R.id.text5) {
            //do something here if button3 is clicked
            // fragmentClass = FragmentNotification.class;
            fragmentClass = FragmentNearestJummah.class;
        } else if (v.getId() == R.id.text6) {
            //do something here if button3 is clicked
            // fragmentClass = FragmentAboutUS.class;
            fragmentClass = FragmentQiblaDirection.class;
        } else if (v.getId() == R.id.text11) {
            //do something here if button3 is clicked
            fragmentClass = FragmentFeedback.class;
        } else if (v.getId() == R.id.text9) {
            //do something here if button3 is clicked
            fragmentClass = FragmentDua.class;
        } else if (v.getId() == R.id.text3) {
            //do something here if button3 is clicked
            fragmentClass = FragmentHome.class;
        } else if (v.getId() == R.id.text10) {
            //do something here if button3 is clicked
            fragmentClass = FragmentSettings.class;
        } else if (v.getId() == R.id.text7) {
            //do something here if button3 is clicked
            fragmentClass = FragmentNotification.class;
        } else if (v.getId() == R.id.text8) {
            //do something here if button3 is clicked
            fragmentClass = FragmentAddMosque.class;
        } else if (v.getId() == R.id.text2) {
            //do something here if button3 is clicked
            fragmentClass = FragmentFavorite.class;
        }


        if(fragment!=null){

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.Screen_Area, fragment ,fragment.getTag()).commit();
        }



        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Screen_Area, fragment).commit();









        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }



    //LOcation Getting Method All working About Location


    @Override
    public void onConnected(@Nullable Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLocation!=null)
        {
           // latLng.setText("Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude());
          //  Toast.makeText(getApplicationContext(),"GOOGLE API:"+mLocation.getLongitude()+"  "+mLocation.getLatitude(),Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getSharedPreferences("LatLang", Context.MODE_PRIVATE).edit();
            editor.putString("Lat", String.valueOf(mLocation.getLatitude()));
            editor.putString("Lag", String.valueOf(mLocation.getLongitude()));
            editor.apply();




        }
        else {

            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        startLocationUpdates();




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


//Location Function.

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }




    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);


    }




    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient,MainActivity.this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {


           // Toast.makeText(getApplicationContext(),"GOOGLE API CL:"+location.getLongitude()+"  "+location.getLatitude(),Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = getSharedPreferences("LatLang", Context.MODE_PRIVATE).edit();
            editor.putString("Lat", String.valueOf(location.getLatitude()));
            editor.putString("Lag", String.valueOf(location.getLongitude()));
            editor.apply();

      /*  Fragment fragmenttttt = getSupportFragmentManager().findFragmentById(R.id.Screen_Area);
        if(fragmenttttt instanceof FragmentHome) {
            try {
    FragmentHome fragment = new FragmentHome();
    FragmentManager manager = getSupportFragmentManager();
    manager.beginTransaction().replace(R.id.Screen_Area, fragment, fragment.getTag()).commit();
}catch (IllegalStateException ex){}
*/
      //  Toast.makeText(getApplicationContext(),"Your Current Location Cordinates:"+location.getLatitude()+""+location.getLongitude(),Toast.LENGTH_SHORT).show();


        }


}//End of Class








