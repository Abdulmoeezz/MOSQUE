package com.example.mymosque;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class AlaramNotificationClassFajar extends IntentService {

    private NotificationManager alarmNotificationManager;
    String  Azan;
    private MediaPlayer mediaPlayer;
    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;




    public AlaramNotificationClassFajar(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendNotification("Its Namaz Fajar Time");
    }


    //handle notification
    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //Create notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);


     /*  Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/fajar_azan");

        Uri alert;

       alert = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + R.raw.fajar_azan);


       //RingtoneManager.getDefaultUri(RingtoneManager.getDefaultType(uri));
       if (alert == null) {
           alert = (Uri) RingtoneManager.getDefaultUri(Integer.parseInt(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI));
          if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);     }
        }

        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
        alamNotificationBuilder.setSound(alert) ;

*/

      /*  Uri alert;

        alert = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + R.raw.fajar_azan);*/

/*
        SharedPreferences prefs = getSharedPreferences("AzanAlaram", Context.MODE_PRIVATE);
        Azan   = prefs.getString("Azan", "N");
        String Fajarr = prefs.getString("AzanFajar",null);

        assert Fajarr != null;
        if(Fajarr.equals("fajr")) {

            SharedPreferences.Editor editor = getSharedPreferences("AzanAlaram", Context.MODE_PRIVATE).edit();
            editor.putString("AzanFajar", "");
            editor.apply();
*/


            //   alamNotificationBuilder.setSound(alert);
            // notiy notification manager about new notification
          //  alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());

            //mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // mediaPlayer.setLooping(false);
            // mediaPlayer.start();


     //   } else{



            mediaPlayer = MediaPlayer.create(this, R.raw.normal_azan);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
           // alamNotificationBuilder.setSound();


            //notiy notification manager about new notification




       // }


//        SharedPreferences prefs = getSharedPreferences("AzanAlaram", Context.MODE_PRIVATE);
//        Azan = prefs.getString("Azan", "N");
//        String Fajarr = prefs.getString("AzanFajar", "T");
//
//
//        if (Fajarr.equals("T")) {
//
//
//            //Start media player
//            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
//            AlarmSoundService.RingtoneStopper stopper = new AlarmSoundService.RingtoneStopper(20000, 1000);
//            stopper.start();
//
//        } else if (Azan.equals("N")) {
//
//
//            //Start media player
//            mediaPlayer = MediaPlayer.create(this, R.raw.normal_azan);
//            mediaPlayer.start();
//            mediaPlayer.setLooping(false);
//            AlarmSoundService.RingtoneStopper stopper = new AlarmSoundService.RingtoneStopper(20000, 1000);
//            stopper.start();
//
//
//        }


    }












}
