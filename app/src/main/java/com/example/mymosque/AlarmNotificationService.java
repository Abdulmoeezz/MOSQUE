package com.example.mymosque;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.support.constraint.Constraints.TAG;

@RequiresApi(api = VERSION_CODES.O)
public class AlarmNotificationService extends IntentService {
    private NotificationManager alarmNotificationManager;
    String  Azan,RestoredAzanText,Alramchecking;
    private MediaPlayer mediaPlayer;

    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {


        //Send notification
        SharedPreferences AzanType = getSharedPreferences("CheckAzan", MODE_PRIVATE);
        RestoredAzanText   = AzanType.getString("AzanType", "S");
        try{

                if (RestoredAzanText.equals("F")) {

                    sendNotification("Namaz Time :Full Azan");

                } else if(RestoredAzanText.equals("S")){

                    ShortsendNotification("Namaz Time :Short Azan");
                }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        }


    //For Full Azan  Azan Notification
    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

       /* Intent cancelAudioBtn = new Intent();
        cancelAudioBtn.setAction("CANCEL_AUDIO");
        PendingIntent cancelAudioIntent = PendingIntent.getBroadcast(this,1231,cancelAudioBtn,PendingIntent.FLAG_UPDATE_CURRENT);

*/

        //Create notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))

                .setContentText(msg).setAutoCancel(false);
        alamNotificationBuilder.setContentIntent(contentIntent);
       // alamNotificationBuilder.addAction(R.drawable.ic_pause_white_48dp,"Stop",cancelAudioIntent);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert alarmNotificationManager != null;
            alamNotificationBuilder.setChannelId("1");
            alarmNotificationManager.createNotificationChannel(notificationChannel);


        }




        mediaPlayer = MediaPlayer.create(this, R.raw.normal_azan);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();


        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());













    }
    //For Short  Azan NOtification

    private void ShortsendNotification(String msg) {

        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //Create notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(false);
        alamNotificationBuilder.setContentIntent(contentIntent);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert alarmNotificationManager != null;
            alamNotificationBuilder.setChannelId("1");
            alarmNotificationManager.createNotificationChannel(notificationChannel);


        }

        mediaPlayer = MediaPlayer.create(this, R.raw.normal_azan_short);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());

    }






















}



