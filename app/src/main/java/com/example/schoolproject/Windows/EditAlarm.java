package com.example.schoolproject.Windows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.schoolproject.ActForScreen;
import com.example.schoolproject.MainActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.ReminderBroadcast;
import com.example.schoolproject.TimePickerFragment;

import java.util.Calendar;

public class EditAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    AppCompatButton btnCh, btnTime;
    ImageButton btnBack;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        btnTime = findViewById(R.id.btn_time);
        btnCh = findViewById(R.id.btn_ch);
        btnBack = findViewById(R.id.btn_back);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        btnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlarm(c);
                Intent intent = new Intent(EditAlarm.this, ActForScreen.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditAlarm.this, ActForScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EditAlarm.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.putLong("timeAlarm", c.getTimeInMillis());
        editor.commit();

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