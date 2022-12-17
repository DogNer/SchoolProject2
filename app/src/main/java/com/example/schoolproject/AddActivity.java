package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schoolproject.Adapter.AchieveAdapter;
import com.example.schoolproject.Modal.GoalModal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    EditText goal_edit;
    AppCompatButton btnAdd;
    private AchieveAdapter adapter;
    private ArrayList<GoalModal> goalArrayList, arrayListAll = new ArrayList<GoalModal>();
    private RecyclerView courseRV;
    public boolean isCheckPunc = false;
    CheckBox checkBox;

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    private FileInputStream inputStream;

    // Класс для работы потоком ввода в файл
    private FileOutputStream outputStream;

    // полный путь к файлу
    private String path = "C:\\hui\\tam.txt";

    public void FileInputOutputStream(String path) {
        this.path = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        goal_edit = findViewById(R.id.str_achieve);
        btnAdd = findViewById(R.id.btn_add);
        checkBox = findViewById(R.id.boxCheck);

        loadData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal = goal_edit.getText().toString();
                if (!goal.isEmpty() && !onlyChar(goal)){
                    if (checkBox.isChecked())
                        goal = checkStr(goal);
                    goalArrayList.add(new GoalModal(goal, false));
                    arrayListAll.add(new GoalModal(goal, false));
                    saveData();
                    Toast.makeText(AddActivity.this, "Achieve is added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this, ActForScreen.class);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(AddActivity.this, "Wrong input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void replaceLeft(char[] mas, int n, int pos){
        for (int i = pos; i < n - 1; ++i) {
            mas[i] = mas[i + 1];
        }
        mas[n - 1] = 0;
    }

    public boolean charEqual(char sing){
        String sep = "!,.:;?@ ";
        for (int i = 0; i < sep.length(); ++i)
            if (sing == sep.charAt(i))
                return true;
        return false;
    }

    public void rightPunctual(char[] mas){
        int i = 0;
        int len = mas.length;
        while (i < len - 1){
            if (charEqual(mas[i + 1]) && charEqual(mas[i])) {
                replaceLeft(mas, len, i + 1);
                len--;
            }
            else ++i;
        }
    }

    public void deleteSpace(char[] mas){
        for (int i = 0; i < mas.length - 1; ++i){
            if ((mas[i] == ' ' && charEqual(mas[i + 1]))) {
                replaceLeft(mas, mas.length, i);
                --i;
            }
        }
    }

    public boolean checkLastChar(char[] mas){
        if (mas[mas.length - 1] == '.'
            || mas[mas.length - 1] == '?' || mas[mas.length - 1] == '!'
            || mas[mas.length - 1] == ';' || Character.isLetterOrDigit(mas.length - 1)){
            return false;
        }
        return true;
    }

    public String convertArrToStr(char[] mas, boolean check){
        String newstr = "";
        for (int i = 0; i < mas.length; ++i){
            if (mas[i] != 0){
                newstr += mas[i];
                if (check && charEqual(mas[i]) && mas[i] != ' ') newstr += " ";
            }
        }
        return newstr;
    }

    public void deleteFirstChar(char[] mas){
        int len = mas.length;
        while(charEqual(mas[0])){
            replaceLeft(mas, len, 0);
        }
    }

    public boolean onlyChar(String str){
        char[] arr;
        arr = str.toCharArray();
        for (int i = 0; i < arr.length; ++i)
            if (Character.isLetterOrDigit(arr[i]))
                return false;
            return true;
    }

    public String checkStr(String res){
        String newstr = "";
        char[] mas, arr;
        mas = res.toCharArray();
        deleteFirstChar(mas);
        deleteSpace(mas);
        rightPunctual(mas);
        deleteSpace(mas);
        newstr = convertArrToStr(mas, true);
        arr = newstr.toCharArray();
        if (checkLastChar(arr) && charEqual(arr[arr.length - 2])){
            arr[arr.length - 2] = '.';
        }
        return convertArrToStr(arr, false);
    }


    private void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(goalArrayList);
        String json_all = gson.toJson(arrayListAll);

        editor.putString("goal", json);
        editor.putString("goal_all", json_all);

        editor.apply();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("goal", null);
        String json_all = sharedPreferences.getString("goal_all", null);

        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();

        goalArrayList = gson.fromJson(json, type);
        arrayListAll = gson.fromJson(json_all, type);

        if (goalArrayList == null) {
            goalArrayList = new ArrayList<>();
        }
        if (arrayListAll == null) {
            arrayListAll = new ArrayList<>();
        }
    }
}