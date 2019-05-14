package com.example.mymosque.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.FavoriteProfileFragment;
import com.example.mymosque.MapsActivity;
import com.example.mymosque.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class AdapterNearestMasajid extends RecyclerView.Adapter<AdapterNearestMasajid.ViewHolder>{



    ArrayList<HashMap<String, String>> modelList;

    private Context mContext;
    double CurrentLatitude,CurrentLongitude,DestinationLatitude,DestinationLongitude;
    String Miles;
    private static final String TAG = "AdapterRVFavorite";




    public AdapterNearestMasajid(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public AdapterNearestMasajid.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new AdapterNearestMasajid.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterNearestMasajid.ViewHolder holder, final int position){

        try {

            SharedPreferences prefs = mContext.getSharedPreferences("LatLang", Context.MODE_PRIVATE);
            CurrentLatitude = Double.parseDouble(prefs.getString("Lat", null));
            CurrentLongitude = Double.parseDouble(prefs.getString("Lag", null));



            if(modelList.get(position).get("favourite_").equals("0")) {


                holder.MasajidName.setText(modelList.get(position).get("name_"));
                String path = modelList.get(position).get("imageurl_");
                holder.MasajidAddress.setText(modelList.get(position).get("address"));
                DestinationLongitude = Double.parseDouble(modelList.get(position).get("longitute_"));
                DestinationLatitude = Double.parseDouble(modelList.get(position).get("latitude_"));
                Miles = meterDistanceBetweenPoints(CurrentLatitude, CurrentLongitude, DestinationLatitude, DestinationLongitude);
                holder.MasajidMiles.setText(Miles);
                holder.FovouriteImage.setBackgroundResource(R.drawable.ic_color_heart_unfill);
                Picasso.get().load(path).into(holder.Image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(mContext, "No Map Found !!", Toast.LENGTH_SHORT).show();
                    }


                });


            }else if(modelList.get(position).get("favourite_").equals("1")){

                holder.MasajidName.setText(modelList.get(position).get("name_"));
                String path = modelList.get(position).get("imageurl_");
                holder.MasajidAddress.setText(modelList.get(position).get("address"));
                DestinationLongitude = Double.parseDouble(modelList.get(position).get("longitute_"));
                DestinationLatitude = Double.parseDouble(modelList.get(position).get("latitude_"));
                Miles = meterDistanceBetweenPoints(CurrentLatitude, CurrentLongitude, DestinationLatitude, DestinationLongitude);
                holder.MasajidMiles.setText(Miles);
                holder.FovouriteImage.setBackgroundResource(R.drawable.ic_color_heart_fill);
                Picasso.get().load(path).into(holder.Image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(mContext, "No Map Found !!", Toast.LENGTH_SHORT).show();
                    }


                });







            }








        }catch (Exception ex){

            holder.MasajidMiles.setText("Not Difined");


        }


        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*    SharedPreferences.Editor editor = mContext.getSharedPreferences("PassFavouriteMosque", MODE_PRIVATE).edit();
                    editor.putString("M_ID", modelList.get(position).get("id_"));
                    editor.putString("M_name", modelList.get(position).get("name_"));
                    editor.putString("M_Image_", modelList.get(position).get("imageurl_"));
                    editor.putString("M_Address_", modelList.get(position).get("address"));
                    editor.putString("M_Longitude_", modelList.get(position).get("longitute_"));
                    editor.putString("M_Latitude_", modelList.get(position).get("latitude_"));
                    editor.putString("miles",Miles);
                    editor.apply();*/

                  /*  AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, new FavoriteProfileFragment()).commit();*/



                SharedPreferences.Editor editor = mContext.getSharedPreferences("Location", MODE_PRIVATE).edit();
                editor.putString("Latitude",modelList.get(position).get("longitute_"));
                editor.putString("Longitude", modelList.get(position).get("latitude_"));
                editor.apply();


                Intent intent = new Intent(mContext, MapsActivity.class);
                mContext.startActivity(intent);










            }
        });







    }//End of On BindHolder


    @Override
    public int getItemCount () {
        return  modelList.size();
    }




    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView MasajidName,MasajidAddress,MasajidMiles;
        ImageView Image,FovouriteImage;
        RelativeLayout Layout;

        public ViewHolder(View itemView) {
            super(itemView);


            MasajidName  = itemView.findViewById(R.id.txt_Masajid);
            Image = itemView.findViewById(R.id.img_);
            Layout= itemView.findViewById(R.id.ItemView_);
            MasajidAddress= itemView.findViewById(R.id.txt_address_);
            MasajidMiles=itemView.findViewById(R.id.TextMiles);
            FovouriteImage=itemView.findViewById(R.id.pic_favorite);

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




