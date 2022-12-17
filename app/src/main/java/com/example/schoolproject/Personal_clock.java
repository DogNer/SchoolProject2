package com.example.schoolproject;

import static android.os.Looper.getMainLooper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.schoolproject.Windows.RegisterWindow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Personal_clock extends AppCompatActivity {

    ImageButton minHour, minMinute, plusMin, plusHour;
    EditText timeHour, timeMinute;
    int cntMin = 0, cntHour = 0;
    AppCompatButton btnSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_clock);

        minHour = findViewById(R.id.nim_cntt_hour);
        minMinute = findViewById(R.id.nim_cntt_min);
        plusHour = findViewById(R.id.plus_cnt_hour);
        plusMin = findViewById(R.id.plus_cnt_min);

        timeHour = findViewById(R.id.time_hour);
        timeMinute = findViewById(R.id.time_min);
        timeHour.setText(String.valueOf(cntHour));
        timeMinute.setText(String.valueOf(cntMin));



        btnSetTime = findViewById(R.id.setTime);
            minHour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cntHour = Integer.valueOf(timeHour.getText().toString());
                    if(cntHour - 1 == -1){
                        minHour.setClickable(false);;
                    }
                    else {
                        minHour.setClickable(true);
                        cntHour--;
                        timeHour.setText(String.valueOf(cntHour));
                    }
                }
            });

            plusHour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cntHour = Integer.valueOf(timeHour.getText().toString());
                    if(cntHour + 1 == 24)
                        minHour.setClickable(false);
                    else {
                        minHour.setClickable(true);
                        cntHour++;
                        timeHour.setText(String.valueOf(cntHour));
                    }
                }
            });

            minMinute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cntMin = Integer.valueOf(timeMinute.getText().toString());
                    if(cntMin - 1 == -1)
                        minMinute.setClickable(false);
                    else {
                        minMinute.setClickable(true);
                        cntMin--;
                        timeMinute.setText(String.valueOf(cntMin));
                    }
                }
            });

            plusMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cntMin = Integer.valueOf(timeMinute.getText().toString());
                    if(cntMin + 1 == 60)
                        plusMin.setClickable(false);
                    else {
                        plusMin.setClickable(true);
                        cntMin++;
                        timeMinute.setText(String.valueOf(cntMin));
                    }
                }
            });

            btnSetTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTimeSet(Integer.valueOf(timeHour.getText().toString()), Integer.valueOf(timeMinute.getText().toString()));
                    /*Intent intent = new Intent(Personal_clock.this, RegisterWindow.class);
                    startActivity(intent);*/
                    finish();
                }
            });
    }

    public void onTimeSet(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Personal_clock.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.putLong("timeAlarm", c.getTimeInMillis());
        editor.commit();

        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}