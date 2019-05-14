package com.example.mymosque;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Notification Message TITLE: " + Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());
//Calling method to generate notification
        sendNotification( remoteMessage.getData());
    }
    //This method is only generating push notification

    private void sendNotification(Map<String, String> row) {




      //  PendingIntent contentIntent = null;Intent.FLAG_ACTIVITY_SINGLE_TOP |



        Intent notificationIntent = new Intent(this, NotificationDetailScreen.class);
       // notificationIntent.putExtra("person","ok hai sub");
       /// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String urrrrl = row.get("url");

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("NotificationFragment", MODE_PRIVATE).edit();
        editor.putString("M_Filename", row.get("file_name"));
        editor.putString("M_Filepath", row.get("file_path"));
        editor.putString("M_Description_", row.get("discription"));
        editor.putString("M_Type", row.get("type"));
        editor.putString("M_Time", row.get("time"));
        editor.putString("N_ID", row.get("n_id"));
        editor.putString("M_ID", row.get("m_id"));
        editor.apply();



        Log.d(TAG, "Notification Message urrrrl: " + urrrrl);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_mosque))
                .setSmallIcon(R.drawable.ic_mosque)
                .setContentTitle("My Mosque")
                .setContentText("Notification From My Mosque")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pIntent);


        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;

    }
}//END OF CLASS
