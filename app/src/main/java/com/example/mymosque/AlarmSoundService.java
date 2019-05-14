package com.example.mymosque;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AlarmSoundService extends Service {
    private MediaPlayer mediaPlayer;
    String  Azan;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

     /*   SharedPreferences prefs = getSharedPreferences("AzanAlaram", Context.MODE_PRIVATE);
        Azan   = prefs.getString("Azan", "N");
      String Fajarr = prefs.getString("AzanFajar","T");


        if(Fajarr.equals("T")){


            //Start media player
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            RingtoneStopper stopper = new RingtoneStopper(20000,1000);
            stopper.start();

        }else if(Azan.equals("N")){


            //Start media player
            mediaPlayer = MediaPlayer.create(this, R.raw.normal_azan);
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
            RingtoneStopper stopper = new RingtoneStopper(20000,1000);
            stopper.start();


        }



*/

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        //On destory stop and release the media player
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

        }
    }






}
