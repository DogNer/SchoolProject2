package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.schoolproject.Windows.LogWindow;
import com.example.schoolproject.Windows.RegisterWindow;
import com.example.schoolproject.receiver.TimeReceiver;

public class MainActivity extends AppCompatActivity {

    AppCompatButton regBtn;
    TextView txName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, TimeReceiver.class));
        regBtn = findViewById(R.id.btn_reg);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sp.getString("user_name", "").isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ActForScreen.class);
            startActivity(intent);
            finish();
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }
}