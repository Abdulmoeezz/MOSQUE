package com.example.mymosque.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymosque.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class AdapterRVTimes extends RecyclerView.Adapter<AdapterRVTimes.ViewHolder> {


    ArrayList<HashMap<String, String>> modelList;
    private Context mContext;


    private static final String TAG = "AdapterRVTimes";



    public AdapterRVTimes(Context Context,ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public AdapterRVTimes.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.namz_times_item, parent, false);
        return new AdapterRVTimes.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterRVTimes.ViewHolder holder, final int position){

      //  holder.Time.setText(NamazTimes.get(position));


        String substring = Objects.requireNonNull(modelList.get(position).get("date_")).substring(Math.max(Objects.requireNonNull(modelList.get(position).get("date_")).length() - 2, 0));
        holder.Date_.setText(substring);
        holder.Fajar_.setText(modelList.get(position).get("fajr_"));
        holder.Zhuhr.setText(modelList.get(position).get("zhuhr_"));
        holder.Asr.setText(modelList.get(position).get("asr__"));
        holder.Maghrib.setText(modelList.get(position).get("maghrib_"));
        holder.Isha.setText(modelList.get(position).get("isha_"));










    }

    @Override
    public int getItemCount () {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Date_,Fajar_,Zhuhr,Asr,Maghrib,Isha;


        public ViewHolder(View itemView) {
            super(itemView);

            Date_= itemView.findViewById(R.id.Text_Date_);
            Fajar_  = itemView.findViewById(R.id.txt_fajr_time_beginning);
            Zhuhr  = itemView.findViewById(R.id.txt_zuhr_time_beginning);
            Asr  = itemView.findViewById(R.id.txt_asr_time_beginning);
            Maghrib  = itemView.findViewById(R.id.txt_maghrib_time_beginning);
            Isha  = itemView.findViewById(R.id.txt_isha_time_beginning);



        }
    }


}

