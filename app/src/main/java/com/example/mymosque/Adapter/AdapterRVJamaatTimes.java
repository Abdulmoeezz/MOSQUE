package com.example.mymosque.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymosque.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdapterRVJamaatTimes extends RecyclerView.Adapter<AdapterRVJamaatTimes.ViewHolder> {


    ArrayList<HashMap<String, String>> modelList;

    private Context mContext;


    private static final String TAG = "AdapterRVJamaatTimes";



    public AdapterRVJamaatTimes(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public AdapterRVJamaatTimes.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jamaat_times_item, parent, false);
        return new AdapterRVJamaatTimes.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterRVJamaatTimes.ViewHolder holder, final int position){
        String substring = Objects.requireNonNull(modelList.get(position).get("date_")).substring(Math.max(Objects.requireNonNull(modelList.get(position).get("date_")).length() - 2, 0));
        holder.Date.setText(substring);


       // holder.Date.setText(modelList.get(position).get("date_"));
        holder.Fajar.setText(modelList.get(position).get("fajr_"));
        holder.Zuhar.setText(modelList.get(position).get("zhuhr_"));
        holder.Asar.setText(modelList.get(position).get("asr__"));
        holder.Magrib.setText(modelList.get(position).get("maghrib_"));
        holder.Isha.setText(modelList.get(position).get("isha_"));










    }

    @Override
    public int getItemCount () {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Date,Fajar,Zuhar,Asar,Magrib,Isha;


        public ViewHolder(View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.Text_Date_);
            Isha = itemView.findViewById(R.id.txt_isha_time_beginning);
            Fajar = itemView.findViewById(R.id.txt_fajr_time_beginning);
            Zuhar = itemView.findViewById(R.id.txt_zuhr_time_beginning);
            Asar = itemView.findViewById(R.id.txt_asr_time_beginning);
            Magrib = itemView.findViewById(R.id.txt_maghrib_time_beginning);



        }
    }


}
