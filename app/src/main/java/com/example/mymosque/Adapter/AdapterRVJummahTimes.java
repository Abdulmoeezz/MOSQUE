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

public class AdapterRVJummahTimes extends RecyclerView.Adapter<AdapterRVJummahTimes.ViewHolder> {


    ArrayList<HashMap<String, String>> modelList;

    private Context mContext;


    private static final String TAG = "RecyclerViewAdapter_F_ADS";



    public AdapterRVJummahTimes(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public AdapterRVJummahTimes.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jummah_times_item, parent, false);
        return new AdapterRVJummahTimes.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterRVJummahTimes.ViewHolder holder, final int position){

      /*  String substring = Objects.requireNonNull(modelList.get(position).get("date_")).substring(Math.max(Objects.requireNonNull(modelList.get(position).get("date_")).length() - 2, 0));
        holder.Date.setText(substring);*/

       holder.Date.setText(modelList.get(position).get("date_"));
        holder.FstJummah.setText(modelList.get(position).get("jummah1"));
        holder.SndJummah.setText(modelList.get(position).get("jummah2"));







    }

    @Override
    public int getItemCount () {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView FstJummah,SndJummah,Date;


        public ViewHolder(View itemView) {
            super(itemView);


            FstJummah  = itemView.findViewById(R.id.txt_1st_jummah);
            SndJummah  = itemView.findViewById(R.id.txt_2nd_jummah);
            Date  = itemView.findViewById(R.id.date_txt_);




        }
    }


}

