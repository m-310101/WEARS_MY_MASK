package com.example.wearsmymask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MILLIS;

public class MainActivity2 extends AppCompatActivity {
    String currentNotifTime;
    static String[] notifContent = new String[10];
    static String[] notifID = new String[10];
    static String[] notifTime = new String[10];
    EditText inputContent;
    EditText inputBox;
    static int notifPosition=0;
    RecyclerView recyclerView;
    static MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button createNotificationButton = findViewById(R.id.addNotification);
        Button cancelNotificationButton = findViewById(R.id.removeNotification);
        ImageButton goBack = findViewById(R.id.imageButton);
        inputBox = (EditText) findViewById(R.id.notifTimeInput);
        inputContent = (EditText) findViewById(R.id.notifContentInput);
        recyclerView = findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(this, notifTime, notifContent);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createNotificationChannel();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createNotificationButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H[:mm[:ss[.SSSSSS]]]");

                currentNotifTime = inputBox.getText().toString();
                notifTime[notifPosition] = currentNotifTime;
                notifContent[notifPosition] = inputContent.getText().toString();
                LocalTime time = java.time.LocalTime.now();
                LocalTime timeNotif = LocalTime.parse(currentNotifTime, dtf);
                long diff = MILLIS.between(time, timeNotif);
                int rc = timeNotif.hashCode();
                notifID[notifPosition] = Integer.toString(rc);
                notifPosition++;
                Intent intent = new Intent(MainActivity2.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity2.this, rc, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + diff, pendingIntent);
                myAdapter.notifyDataSetChanged();
            }
        });

        cancelNotificationButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H[:mm[:ss[.SSSSSS]]]");
                currentNotifTime = inputBox.getText().toString();
                LocalTime timeNotif = LocalTime.parse(currentNotifTime, dtf);
                Intent intent = new Intent(MainActivity2.this, ReminderBroadcast.class);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity2.this, timeNotif.hashCode(), intent, 0);
                alarmManager.cancel(pendingIntent);

                for (int j=0; j<9; j++){
                    if (notifID[j].equals(Integer.toString(timeNotif.hashCode()))){
                        for (int q=j; q<9; q++){
                            notifTime[q] = notifTime[q+1];
                            notifID[q] = notifID[q+1];
                            notifContent[q] = notifContent[q+1];
                        }
                        notifTime[9] = "";
                        notifID[9] = "";
                        notifContent[9] = "";
                        notifPosition=notifPosition-1;
                        j=10;
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openActivity();}
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "notifChannel";
            String description = "Channel for reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void openActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}