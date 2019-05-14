package com.example.mymosque;




import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Fragments.FragementProfile;
import com.example.mymosque.Fragments.FragmentFeedback;
import com.example.mymosque.Fragments.FragmentNearestMasajid;
import com.example.mymosque.Fragments.FragmentPrayerTimes;
import com.example.mymosque.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.google.maps.android.SphericalUtil.computeDistanceBetween;


public class AdapterRVFavorite extends RecyclerView.Adapter<com.example.mymosque.AdapterRVFavorite.ViewHolder>{



    ArrayList<HashMap<String, String>> modelList;
    private Context mContext;
    double CurrentLatitude,CurrentLongitude,DestinationLatitude,DestinationLongitude,rad;
    String Miles;
    private static final String TAG = "AdapterRVFavorite";




    public AdapterRVFavorite(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public com.example.mymosque.AdapterRVFavorite.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new com.example.mymosque.AdapterRVFavorite.ViewHolder(view);


    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder (@NonNull com.example.mymosque.AdapterRVFavorite.ViewHolder holder, final int position){

         try {

             SharedPreferences prefs = mContext.getSharedPreferences("LatLang", Context.MODE_PRIVATE);
             CurrentLatitude = Double.parseDouble(prefs.getString("Lat", null));
             CurrentLongitude = Double.parseDouble(prefs.getString("Lag", null));

             holder.MasajidName.setText(modelList.get(position).get("name_"));
             String path = modelList.get(position).get("imageurl_");
             holder.MasajidAddress.setText(modelList.get(position).get("address"));

             DestinationLongitude = Double.parseDouble(modelList.get(position).get("longitute_"));
             DestinationLatitude = Double.parseDouble(modelList.get(position).get("latitude_"));



             LatLng CurrentLOcation= new LatLng(CurrentLatitude, CurrentLongitude);
             LatLng DesLOcation= new LatLng(DestinationLatitude, DestinationLongitude);

             rad=computeDistanceBetween(CurrentLOcation,DesLOcation);

             Miles = String.format("%.2f",rad/1609.344);






            // Miles = meterDistanceBetweenPoints(CurrentLatitude, CurrentLongitude, DestinationLatitude, DestinationLongitude);
             holder.MasajidMiles.setText(Miles);


             Picasso.get().load(path).into(holder.Image, new com.squareup.picasso.Callback() {
                 @Override
                 public void onSuccess() {


                 }

                 @Override
                 public void onError(Exception e) {
                     Toast.makeText(mContext, "No Map Found !!", Toast.LENGTH_SHORT).show();
                 }


             });

             holder.Layout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     SharedPreferences.Editor editor = mContext.getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE).edit();
                     editor.putString("M_ID", modelList.get(position).get("id_"));
                     editor.putString("M_name", modelList.get(position).get("name_"));
                     editor.putString("M_Image_", modelList.get(position).get("imageurl_"));
                     editor.putString("M_Address_", modelList.get(position).get("address"));
                     editor.putString("M_Longitude_", modelList.get(position).get("longitute_"));
                     editor.putString("M_Latitude_", modelList.get(position).get("latitude_"));

                     editor.putString("miles",Miles);
                     editor.apply();

                     AppCompatActivity activity = (AppCompatActivity) v.getContext();
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, new FavoriteProfileFragment()).commit();

                 }
             });



         }catch (Exception ex){

             holder.MasajidMiles.setText("Not Difined");


         }
    }//End of On BindHolder


    @Override
    public int getItemCount () {
        return  modelList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView MasajidName,MasajidAddress,MasajidMiles;
        ImageView Image;
        RelativeLayout Layout;

        public ViewHolder(View itemView) {
            super(itemView);


            MasajidName  = itemView.findViewById(R.id.txt_Masajid);
            Image = itemView.findViewById(R.id.img_);
            Layout= itemView.findViewById(R.id.ItemView_);
            MasajidAddress= itemView.findViewById(R.id.txt_address_);
            MasajidMiles=itemView.findViewById(R.id.TextMiles);

        }
    }


    @SuppressLint("DefaultLocale")
    @org.jetbrains.annotations.NotNull
    private String meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return String.format("%.2f",6366000 * tt);
    }








}



