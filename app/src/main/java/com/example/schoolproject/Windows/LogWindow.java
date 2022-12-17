package com.example.schoolproject.Windows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.schoolproject.MainActivity;
import com.example.schoolproject.R;


public class LogWindow extends AppCompatActivity {

    ImageButton btnBack;
    EditText strName, strEmail, strPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_window);

        strName = findViewById(R.id.str_name);
        strEmail = findViewById(R.id.str_email);
        strPass = findViewById(R.id.str_pass);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogWindow.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}