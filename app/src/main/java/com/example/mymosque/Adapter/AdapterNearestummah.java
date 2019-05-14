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
import com.example.mymosque.NotificationDetailScreen;
import com.example.mymosque.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;




public class AdapterNearestummah extends RecyclerView.Adapter<AdapterNearestummah.ViewHolder>{



        ArrayList<HashMap<String, String>> modelList;
        private Context mContext;
      //  double CurrentLatitude,CurrentLongitude,DestinationLatitude,DestinationLongitude;
        String Miles;
        private static final String TAG = "AdapterNearestummah";




        public AdapterNearestummah(Context Context, ArrayList<HashMap<String, String>> Names) {
            this.mContext = Context;
            this.modelList = Names;

        }
        @NonNull
        @Override
        public AdapterNearestummah.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearesjummah, parent, false);
            return new AdapterNearestummah.ViewHolder(view);


        }

        @Override
        public void onBindViewHolder (@NonNull AdapterNearestummah.ViewHolder holder, final int position){

            try {

              /*  SharedPreferences prefs = mContext.getSharedPreferences("LatLang", Context.MODE_PRIVATE);
                CurrentLatitude = Double.parseDouble(prefs.getString("Lat", ""));
                CurrentLongitude = Double.parseDouble(prefs.getString("Lag", ""));
*/
                holder.MasajidName.setText(modelList.get(position).get("name_"));
                String path = modelList.get(position).get("imageurl_");
                holder.MasajidAddress.setText(modelList.get(position).get("address"));
                holder.date.setText(modelList.get(position).get("timestamp"));
                holder.jummahFirst.setText(modelList.get(position).get("Jummah1_"));
                holder.JummahSecond.setText(modelList.get(position).get("Jummah2_"));
/*
                DestinationLongitude = Double.parseDouble(modelList.get(position).get("longitute_"));
                DestinationLatitude = Double.parseDouble(modelList.get(position).get("latitude_"));

                Miles = meterDistanceBetweenPoints(CurrentLatitude, CurrentLongitude, DestinationLatitude, DestinationLongitude);*/
              //  holder.MasajidMiles.setText(Miles);


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

                        SharedPreferences.Editor editor = mContext.getSharedPreferences("Location", MODE_PRIVATE).edit();
                        editor.putString("Longitude",modelList.get(position).get("longitude") );
                        editor.putString("Latitude", modelList.get(position).get("lattitude") );
                        editor.apply();



                        Intent intent= new Intent(mContext, MapsActivity.class);
                        mContext.startActivity(intent);
                    }
                });




            }catch (Exception ex){

                ///holder.MasajidMiles.setText("Not Difined");


            }
        }//End of On BindHolder


        @Override
        public int getItemCount () {
            return  modelList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {

            TextView MasajidName,MasajidAddress,date,jummahFirst,JummahSecond;
            ImageView Image;
            RelativeLayout Layout;

            public ViewHolder(View itemView) {
                super(itemView);

                MasajidName  = itemView.findViewById(R.id.txt_Masajid);
                Image = itemView.findViewById(R.id.img_);
                Layout= itemView.findViewById(R.id.ItemView_);
                MasajidAddress= itemView.findViewById(R.id.txt_address_);
                date=itemView.findViewById(R.id.TextDate);
                jummahFirst=itemView.findViewById(R.id.FirstJummahTime);
                JummahSecond=itemView.findViewById(R.id.SecondJummahTime);



            }
        }
        private String meterDistanceBetweenPoints(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return String.format("%.2f",dist);
    }
        private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
        private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }






    }




