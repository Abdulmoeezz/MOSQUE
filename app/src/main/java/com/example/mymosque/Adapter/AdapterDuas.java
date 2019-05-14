package com.example.mymosque.Adapter;

/*
public class AdapterDuas {
}
*/

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.DuaDetailsScreen;
import com.example.mymosque.ItemClickListener;
import com.example.mymosque.MapsActivity;
import com.example.mymosque.Model.DuaList;
import com.example.mymosque.Model.MosqueData;
import com.example.mymosque.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static android.content.Context.MODE_PRIVATE;


public class AdapterDuas extends RecyclerView.Adapter<AdapterDuas.ReyclerViewHolder> {


    Context context;
    ArrayList<DuaList> modelList;
    private static final String TAG = "AdapterDuas";
    String audio_url_;
    private static MediaPlayer  md;
    int ValueSeekBar;
    Runnable run;
    ProgressDialog PG;
    String Filepath,Filename;
    private int expanded_position_ = -1;
    SharedPreferences.Editor editor;

    Handler seekHandler = new Handler();

    public AdapterDuas(Context context,ArrayList<DuaList> list) {
        this.context = context;
        this.modelList=list;
        this.PG=new ProgressDialog(context);
        PG.setMessage("Preparing Your Audio");
        PG.setCancelable(false);


    }


    @NonNull
    @Override
    public ReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duas, parent, false);
        return new ReyclerViewHolder(v);


    }




    @Override
    public void onBindViewHolder(@NonNull final ReyclerViewHolder holder, int position) {

        DuaList putData = modelList.get(position);


        holder.Title_.setText(putData.getName());
       // holder.Text_Dua_.setText(putData.getDiscription());
       // holder.Text_Translate_.setText(putData.getDiscriptionArbic());
        holder.DuaNumber.setText(String.valueOf(putData.getD_id()));
        Filepath = putData.getFile_path();
        Filename = putData.getFile_name();
        audio_url_="http://masjidi.co.uk/"+ Filepath + Filename;
/*

        if (position == expanded_position_) {
            holder.StopButton.setVisibility(View.GONE);

        } else {

            holder.PlayButton.setVisibility(View.VISIBLE);


        }
*/



holder.Layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        editor = context.getSharedPreferences("DuaDataPass", MODE_PRIVATE).edit();
        editor.putString("Title",putData.getName());
        editor.putString("DuaNumber", String.valueOf(putData.getD_id()));
        editor.putString("TextDua", putData.getDiscription());
        editor.putString("TextDuaTranslation", putData.getDiscriptionArbic());
        editor.putString("AudioUrl","http://masjidi.co.uk/"+putData.getFile_path()+ putData.getFile_name());
        editor.apply();


        Intent intent = new Intent(context, DuaDetailsScreen.class);
        context.startActivity(intent);










    }
});


/**//*
        holder.PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expanded_position_ = holder.getAdapterPosition();

                PG.show();
                try{
                    Filepath = modelList.get(position).get("file_path_url_");
                    Filename = modelList.get(position).get("file_name_");
                    audio_url_="http://masjidi.co.uk/"+ Filepath + Filename;


                    if (md != null && md.isPlaying()){
                        md.stop();
                        md.reset();
                        md.release();
                        md = null;

                        holder.SeekbarMediaPlayer.setMax(0);
                        holder.SeekbarMediaPlayer.setProgress(0);




                    }
                    md = new MediaPlayer();
                    // md= MediaPlayer.create(v.getContext(),Uri.parse(audio_url_) );
                    md.setDataSource(context, Uri.parse(audio_url_));
                    md.prepare();
                    md.start();

                    holder.SeekbarMediaPlayer.setMax(md.getDuration());
                    holder.StopButton.setVisibility(View.VISIBLE);
                    holder.PlayButton.setVisibility(View.INVISIBLE);


                    if (md.isPlaying()) {
                        PG.dismiss();
                    }

                    run = new Runnable() {
                        @Override
                        public void run() {
                            try {

                                holder.SeekbarMediaPlayer.setProgress(md.getCurrentPosition());
                                seekHandler.postDelayed(run, 100);

                            } catch (Exception ex) {
                            }

                        }
                    };
                    run.run();

                    md.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            holder.StopButton.setVisibility(View.INVISIBLE);
                            holder.PlayButton.setVisibility(View.VISIBLE);
                            holder.SeekbarMediaPlayer.setMax(0);
                            holder.SeekbarMediaPlayer.setProgress(0);


                        }
                    });



                }catch (Exception ex){ ex.printStackTrace();}



            }


        });//playbutton


        holder.SeekbarMediaPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ValueSeekBar=progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                try {
                    md.seekTo(ValueSeekBar);
                }catch (Exception ex){

                    ex.printStackTrace();

                }


            }
        });



        holder.StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{



                    if(md.isPlaying()){
                        md.stop();
                        md.reset();
                        md.release();
                        md=null;
                        holder.StopButton.setVisibility(View.INVISIBLE);
                        holder.PlayButton.setVisibility(View.VISIBLE);
                    }

                }catch (Exception ex){ ex.printStackTrace();}
            }
        });//Stopbutton*/



    }//END OF ONBINDVVIEWHOLDER

    public void filterList(ArrayList<DuaList> filteredList) {
        modelList = filteredList;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return  modelList.size();
    }

    public class ReyclerViewHolder extends RecyclerView.ViewHolder {

        TextView DuaNumber,Title_,Text_Dua_,Text_Translate_;
        SeekBar  SeekbarMediaPlayer;
        ImageView PlayButton,StopButton;
        RelativeLayout Layout;



        public ReyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            DuaNumber=itemView.findViewById(R.id.Txt_Number_);
            Title_=itemView.findViewById(R.id.Txt_Title);
           // Text_Dua_=itemView.findViewById(R.id.Text_dua_);
          //  Text_Translate_=itemView.findViewById(R.id.Text_DuaTranslation_);
            Layout=itemView.findViewById(R.id.dua_layout_);

           /* SeekbarMediaPlayer=itemView.findViewById(R.id.Seekbar_play_);
            PlayButton=itemView.findViewById(R.id.Image_Play_);
            StopButton=itemView.findViewById(R.id.Image_Stop_);*/
           // StopButton.setVisibility(View.INVISIBLE);







        }




    }








}
