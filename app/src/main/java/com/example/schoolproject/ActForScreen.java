package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolproject.Adapter.ViewPagerAdapter;
import com.example.schoolproject.Screen.AddFragment;
import com.example.schoolproject.Screen.HomeFragment;
import com.example.schoolproject.Screen.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class ActForScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    AddFragment addFragment = new AddFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_for_screen);

        bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment)
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_ic:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment)
                                .commit();
                        return true;
                    case R.id.prof_ic:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, profileFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });


    }


}