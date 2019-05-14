package com.example.mymosque.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mymosque.Fragments.FragmentNearestJummah;
import com.example.mymosque.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class AdapterPostalCode extends RecyclerView.Adapter<AdapterPostalCode.ViewHolder> {


    ArrayList<HashMap<String, String>> modelList;
    private Context mContext;
    private static final String TAG = "RecyclerViewAdapter_F_ADS";



    public AdapterPostalCode(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public AdapterPostalCode.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postalcode_, parent, false);
        return new AdapterPostalCode.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterPostalCode.ViewHolder holder, final int position){

      /*  String substring = Objects.requireNonNull(modelList.get(position).get("date_")).substring(Math.max(Objects.requireNonNull(modelList.get(position).get("date_")).length() - 2, 0));
        holder.Date.setText(substring);*/

holder.PostalCode.setText(modelList.get(position).get("postalcode"));
holder.layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = mContext.getSharedPreferences("PostalCodePass", MODE_PRIVATE).edit();
        editor.putString("PostalCode", modelList.get(position).get("postalcode"));
        editor.apply();



        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = new FragmentNearestJummah();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, myFragment).addToBackStack(null).commit();



    }
});





    }

    @Override
    public int getItemCount () {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView PostalCode;
        RelativeLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);


            PostalCode  = itemView.findViewById(R.id.postalcode_Number);
            layout      =itemView.findViewById(R.id.ItemLayoutPostalCodes);




        }
    }


}

