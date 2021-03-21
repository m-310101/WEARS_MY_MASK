package com.example.wearsmymask;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("WEARS MY MASK")
                .setContentText(MainActivity2.notifContent[0]);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(200, builder.build());

        for (int j=0; j<9; j++){
            MainActivity2.notifTime[j]=MainActivity2.notifTime[j+1];
            MainActivity2.notifID[j]=MainActivity2.notifID[j+1];
            MainActivity2.notifContent[j]=MainActivity2.notifContent[j+1];
        }
        MainActivity2.notifTime[9]="";
        MainActivity2.notifID[9]="";
        MainActivity2.notifContent[9]="";
        MainActivity2.notifPosition=MainActivity2.notifPosition - 1;
        MainActivity2.myAdapter.notifyDataSetChanged();
    }

}
