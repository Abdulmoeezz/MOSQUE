package com.example.mymosque.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.mymosque.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;






public class AdapterAskimamRV extends RecyclerView.Adapter<AdapterAskimamRV.ViewHolder>{



    ArrayList<HashMap<String, String>> modelList;

    private Context mContext;
    double CurrentLatitude,CurrentLongitude,DestinationLatitude,DestinationLongitude;
    String Miles;
    private static final String TAG = "AdapterRVFavorite";




    public AdapterAskimamRV(Context Context, ArrayList<HashMap<String, String>> Names) {
        this.mContext = Context;
        this.modelList = Names;

    }
    @NonNull
    @Override
    public    AdapterAskimamRV.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_askimam_question_answers, parent, false);
        return new AdapterAskimamRV.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder (@NonNull AdapterAskimamRV.ViewHolder holder, final int position){

        if (Objects.equals(modelList.get(position).get("answer"), "")) {

            holder.AnswerByImam_Right.setText("Waiting For Imam Answer");
            holder.QuestionByUser_Left.setText(modelList.get(position).get("questiion_"));
        }else{


            holder.AnswerByImam_Right.setText(modelList.get(position).get("answer"));
            holder.QuestionByUser_Left.setText(modelList.get(position).get("questiion_"));
        }


    }//End of On BindHolder


    @Override
    public int getItemCount () {
        return  modelList.size();
    }




    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView AnswerByImam_Right,QuestionByUser_Left;



        public ViewHolder(View itemView) {
            super(itemView);


            AnswerByImam_Right  = itemView.findViewById(R.id.AnswerByImam_Right);
            QuestionByUser_Left= itemView.findViewById(R.id.QuestionByUser_Left);


        }
    }










}



