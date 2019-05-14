package com.example.mymosque;

/*public class RV_FindMasajid {
}*/


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

import com.example.mymosque.Adapter.OnBottomReachedListener;
import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.Fragments.FragementProfile;
import com.example.mymosque.Fragments.FragmentFeedback;
import com.example.mymosque.Fragments.FragmentHome;
import com.example.mymosque.Fragments.FragmentNearestMasajid;
import com.example.mymosque.Model.Favourite;
import com.example.mymosque.Model.MosqueData;
import com.example.mymosque.Model.USERID;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mymosque.R.drawable.ic_color_heart_unfill;
import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class RV_FindMasajid extends RecyclerView.Adapter<RV_FindMasajid.ViewHolder> implements View.OnClickListener {



    ArrayList<MosqueData> modelList;
    double CurrentLatitude,CurrentLongitude,DestinationLatitude,DestinationLongitude;
    double Miles;
    OnBottomReachedListener onBottomReachedListener;

    private Context mContext;
    String UserID;


    private static final String TAG = "RV_FindMasajid";



    public RV_FindMasajid(Context Context , ArrayList<MosqueData> Names, String UserID) {
        this.mContext = Context;
        this.modelList = Names;
        this.UserID =UserID;

    }




    @NonNull
    @Override
    public RV_FindMasajid.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_masajid_item, parent, false);
        return new RV_FindMasajid.ViewHolder(view);


    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder (@NonNull RV_FindMasajid.ViewHolder holder, final int position){
        MosqueData putData = modelList.get(position);
        if(putData.getFarvoriate() == 0){

            try {

                SharedPreferences prefs = mContext.getSharedPreferences("LatLang", Context.MODE_PRIVATE);
                CurrentLatitude = Double.parseDouble(prefs.getString("Lat", null));
                CurrentLongitude = Double.parseDouble(prefs.getString("Lag", null));

                holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_unfill);
                holder.MasajidName.setText(putData.getName());
                holder.MasjidAddress.setText(putData.getAddress());
                DestinationLongitude = Double.parseDouble(putData.getLongtitude());
                DestinationLatitude = Double.parseDouble(putData.getLatitude());

              //  Miles =meterDistanceBetweenPoints(CurrentLatitude, CurrentLongitude, DestinationLatitude, DestinationLongitude);

             /*   LatLng CurreLatlng=new LatLng(CurrentLatitude,CurrentLongitude);
               // LatLng DesLatlng=new LatLng(DestinationLatitude,DestinationLongitude);
                LatLng DesLatlng=new LatLng(31.4919461,74.2681939);




                Miles =computeDistanceBetween(CurreLatlng,DesLatlng);*/
                holder.Textmiles.setText(String.format("%.2f",putData.getMiles()));










                String path = putData.getImageurl();
                Picasso.get().load(path).into(holder.Image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(mContext, "No Map Found !!", Toast.LENGTH_SHORT).show();
                    }


                });

            }catch (Exception ex){

                holder.Textmiles.setText("not defined");

            }


        }else if(putData.getFarvoriate() == 1){

            try {

                SharedPreferences prefs = mContext.getSharedPreferences("LatLang", Context.MODE_PRIVATE);
                CurrentLatitude = Double.parseDouble(prefs.getString("Lat", null));
                CurrentLongitude = Double.parseDouble(prefs.getString("Lag", null));



                holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_fill);
                holder.MasajidName.setText(putData.getName());
                holder.MasjidAddress.setText(putData.getAddress());
                DestinationLongitude = Double.parseDouble(putData.getLongtitude());
                DestinationLatitude = Double.parseDouble(putData.getLatitude());


              /*LatLng CurreLatlng=new LatLng(CurrentLatitude,CurrentLongitude);
                LatLng DesLatlng=new LatLng(DestinationLatitude,DestinationLongitude);




                Miles =computeDistanceBetween(CurreLatlng,DesLatlng);*/
                holder.Textmiles.setText(String.format("%.2f",putData.getMiles()));


                String path = putData.getImageurl();
                Picasso.get().load(path).into(holder.Image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(mContext, "No Map Found !!", Toast.LENGTH_SHORT).show();
                    }


                });
            }catch (Exception ex){

                holder.Textmiles.setText("not defined");

            }

        }




        holder.LikeImage_.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (putData.getFarvoriate_id() == -1 && putData.getFarvoriate() == 0 ){

                   holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_fill);

                   Toast.makeText(mContext,"Added to Favourite : ",Toast.LENGTH_LONG).show();

                   SetFavouriteServer(putData.getID(),Integer.valueOf(UserID));






               }else if ( putData.getFarvoriate_id() != -1 && putData.getFarvoriate() == 1 ){
                   holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_unfill);
                   Toast.makeText(mContext,"Un-Favourite : ",Toast.LENGTH_LONG).show();

                   SetUnFavouriteServer(putData.getFarvoriate_id(),0,position,v);






               }/*else if (modelList.get(position).get("favourite_").equals("1")) {
                   holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_unfill);
                   SetUnFavouriteServer(Integer.parseInt(modelList.get(position).get("favourite_id_")), 0);



               }*/
               else if ( putData.getFarvoriate_id() != -1 && putData.getFarvoriate() == 0  ){
                       holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_fill);

                        Toast.makeText(mContext,"Added to Favourite : ",Toast.LENGTH_LONG).show();

                        SetUnFavouriteServer(putData.getFarvoriate_id(),1,position,v);





               }/*else if (modelList.get(position).get("favourite_").equals("0") ) {
                   holder.LikeImage_.setBackgroundResource(R.drawable.ic_color_heart_fill);
                   SetUnFavouriteServer(Integer.parseInt(modelList.get(position).get("favourite_id_")), 1);
                  // Refresh(v);


               }*/








               }












       });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PassMosqueData", MODE_PRIVATE).edit();
                editor.putString("M_ID",String.valueOf(putData.getID()) );
                editor.putString("M_name",putData.getName());
                editor.putString("M_Image_",putData.getImageurl());
                editor.putString("M_address_",putData.getAddress());
                editor.putString("M_Miles_",String.format("%.2f",putData.getMiles()));
                editor.putString("M_Longitude_", String.valueOf(DestinationLongitude));
                editor.putString("M_Latitude_", String.valueOf(DestinationLatitude));
                editor.apply();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragementProfile()).commit();
            }
        });








    }//end of ViewHolder Function




    public void filterList(ArrayList<MosqueData> filteredList) {
        modelList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount () {
        return modelList.size();
    }

    @Override
    public void onClick(View v) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView MasajidName,MasjidAddress,Textmiles;
        ImageView Image,LikeImage_;

        public ViewHolder(View itemView) {
            super(itemView);


            MasajidName  = itemView.findViewById(R.id.txt_Masajid);
            Image = itemView.findViewById(R.id.img_);
            LikeImage_=itemView.findViewById(R.id.Like_Image_);
            MasjidAddress=itemView.findViewById(R.id.MasajidAddress);
            Textmiles=itemView.findViewById(R.id.TextMiles);



        }
    }

    public  void  SetFavouriteServer(int M,int U){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<Favourite> call = service.SetFavourite(M,U);

        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {

                //Toast.makeText(mContext,"Add to the Favourite",Toast.LENGTH_LONG);
                Toast.makeText(mContext,"Add to the Favourite : "+response.body().getName(),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {


                Toast.makeText(mContext,"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });










    }//End of Function


    public  void  SetUnFavouriteServer(int ID,int F,int p,View v){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<Favourite> call = service.Unfavourite(ID,F);

        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {

                // Log.e("okayjanu",response.toString());
               /* AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentHome()).commit();*/


                modelList.get(p).setFarvoriate_id(response.body().getFarvoriate_id());
                modelList.get(p).setFarvoriate(response.body().getFarvoriate());








            }

            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {


                Toast.makeText(mContext,"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });

    }//End of Function


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



