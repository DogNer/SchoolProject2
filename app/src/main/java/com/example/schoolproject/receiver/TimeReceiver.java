package com.example.schoolproject.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.example.schoolproject.Modal.GoalModal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeReceiver extends Service {

    private ArrayList<GoalModal> goalArrayList = new ArrayList<GoalModal>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 57);
        today.set(Calendar.HOUR_OF_DAY, 14);

        if (System.currentTimeMillis() >= today.getTimeInMillis()){
            goalArrayList.clear();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }


}
