package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.schoolproject.Windows.LogWindow;
import com.example.schoolproject.Windows.RegisterWindow;

public class MainActivity extends AppCompatActivity {

    AppCompatButton regBtn, logBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regBtn = findViewById(R.id.btn_reg);
        logBtn = findViewById(R.id.btn_log);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterWindow.class);
                startActivity(intent);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogWindow.class);
                startActivity(intent);
            }
        });
    }
}