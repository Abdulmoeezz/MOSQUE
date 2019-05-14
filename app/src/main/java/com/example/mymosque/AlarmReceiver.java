package com.example.mymosque;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {




        SharedPreferences checkAlram = context.getSharedPreferences("AlaramCheck", MODE_PRIVATE);
        String Alramchecking   = checkAlram.getString("Switch", "on");

        if(Alramchecking.equals("off")){


            Toast.makeText(context, " AR:Broadcast Recevier Calling  but Alaram is  Off", Toast.LENGTH_SHORT).show();





        }else if(Alramchecking.equals("on")){

            Toast.makeText(context, " AR:Broadcast Recevier Calling", Toast.LENGTH_LONG).show();
            ComponentName comp = new ComponentName(context.getPackageName(),AlarmNotificationService.class.getName());
            startWakefulService(context,(intent.setComponent(comp)));




        }





    }


}
