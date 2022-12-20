package com.example.schoolproject.Windows;

import static android.os.Looper.getMainLooper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolproject.ActForScreen;
import com.example.schoolproject.CustomClock;
import com.example.schoolproject.MainActivity;
import com.example.schoolproject.Personal_clock;
import com.example.schoolproject.R;
import com.example.schoolproject.ReminderBroadcast;
import com.example.schoolproject.TimePickerFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterWindow extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    ImageButton btnBack;
    EditText nameEdit;
    AppCompatButton btnReg, btnSel;
    String nameStr;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    CustomClock customClock;

    ImageView arrowMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_window);
        nameEdit = findViewById(R.id.str_name);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterWindow.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        nameEdit.setText(sharedPreferences.getString("user_name", ""));
        btnSel = findViewById(R.id.btn_time);
        btnSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();

                editor.putString("user_name", name);
                editor.putInt("cntDay", 0);
                editor.commit();
                /*DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");*/
                //showDialogInfo();
                /*CustomClock customClock = new CustomClock(RegisterWindow.this);
                customClock.showContextMenu();*/
                Intent intent = new Intent(RegisterWindow.this, Personal_clock.class);
                startActivity(intent);
            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterWindow.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnReg = findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(RegisterWindow.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterWindow.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("user_name", name);
                    editor.commit();

                    Intent intent = new Intent(RegisterWindow.this, ActForScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterWindow.this);
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

    private void showDialogInfo() {
        final Dialog dialog = new Dialog(RegisterWindow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dg_clock);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}