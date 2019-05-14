package com.example.mymosque.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.NotificationDetailScreen;
import com.example.mymosque.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AdapterNotificationRV extends RecyclerView.Adapter<com.example.mymosque.Adapter.AdapterNotificationRV.ViewHolder> {


        ArrayList<HashMap<String, String>> modelList;
        private Context mContext;


        private static final String TAG = "AdapterNotificationRV";



        public AdapterNotificationRV(Context Context,ArrayList<HashMap<String, String>> Names) {
            this.mContext = Context;
            this.modelList = Names;

        }
        @NonNull
        @Override
        public com.example.mymosque.Adapter.AdapterNotificationRV.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_rv, parent, false);
            return new com.example.mymosque.Adapter.AdapterNotificationRV.ViewHolder(view);


        }

        @Override
        public void onBindViewHolder (@NonNull com.example.mymosque.Adapter.AdapterNotificationRV.ViewHolder holder, final int position){



            holder.Description.setText(modelList.get(position).get("Description_"));
            holder.TimeStamp.setText(modelList.get(position).get("Time_"));


            holder.Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("NotificationFragment", MODE_PRIVATE).edit();
                    editor.putString("M_Filename", modelList.get(position).get("Filename_"));
                    editor.putString("M_Filepath", modelList.get(position).get("Filepath_"));
                    editor.putString("M_Description_", modelList.get(position).get("Description_"));
                    editor.putString("M_Type", modelList.get(position).get("Type_"));
                    editor.putString("M_Time", modelList.get(position).get("Time_"));
                    editor.putString("N_ID", modelList.get(position).get("N_id"));
                    editor.putString("M_ID",modelList.get(position).get("M_id"));
                    editor.apply();
                    Intent intent= new Intent(mContext, NotificationDetailScreen.class);
                    mContext.startActivity(intent);



                }
            });





        }

        @Override
        public int getItemCount () {
            return modelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView Description,TimeStamp;
            RelativeLayout  Layout;



            public ViewHolder(View itemView) {
                super(itemView);

                Description= itemView.findViewById(R.id.Text_Description);
                TimeStamp  = itemView.findViewById(R.id.Time_);
                Layout=itemView.findViewById(R.id.layoutNotification);





            }
        }


    }

