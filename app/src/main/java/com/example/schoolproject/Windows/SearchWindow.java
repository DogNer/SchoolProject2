package com.example.schoolproject.Windows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.schoolproject.ActForScreen;
import com.example.schoolproject.Adapter.AchieveAdapter;
import com.example.schoolproject.Adapter.AdapterSearch;
import com.example.schoolproject.Modal.GoalModal;
import com.example.schoolproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchWindow extends AppCompatActivity {

    EditText searchEditText;
    private AdapterSearch adapterSearch;
    private ArrayList<GoalModal> goalArrayList;
    private RecyclerView goalRV;;
    private AppCompatButton btnSeerch;
    LinearLayout notMachText;
    ImageView btnClose;
    int cnt = 0;
    LinearLayout btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_window);

        searchEditText = findViewById(R.id.searchEdit);
        goalRV = findViewById(R.id.recyclerView);
        btnSeerch = findViewById(R.id.btn_search);
        notMachText = findViewById(R.id.notMach);
        btnClose = findViewById(R.id.close_btn);
        btnClear = findViewById(R.id.clearTask);

        loadData();
        buildRecyclerView(searchEditText.getText().toString());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchWindow.this, ActForScreen.class);
                startActivity(intent);
                finish();
            }
        });

        btnSeerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!charEqual(searchEditText.getText().toString())) {
                    loadData();
                    buildRecyclerView(searchEditText.getText().toString());

                    if (isWordInList(searchEditText.getText().toString()))
                        notMachText.setVisibility(View.GONE);
                    else notMachText.setVisibility(View.VISIBLE);
                }
                else {
                    loadData();
                    buildRecyclerView("");
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearNonAchieve();
            }
        });
    }

    public void clearNonAchieve(){
        for (int i = 0; i < goalArrayList.size(); ++i){
            if (!goalArrayList.get(i).isDoneAch()){
                goalArrayList.remove(i);
                --i;
            }
        }
        saveData();
        loadData();
        buildRecyclerView("");
    }

    public boolean isWordInList(String word){
        for (int i = 0; i < goalArrayList.size(); ++i){
            if (goalArrayList.get(i).getGaol().indexOf(word) != -1) return true;
        }
        return false;
    }
    public static boolean charEqual(String str){
        boolean check;
        String sep = ",.;: ";
        for (int i = 0; i < str.length(); ++i)
            if (Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        return true;
    }

    private void buildRecyclerView(String word) {
        adapterSearch = new AdapterSearch(goalArrayList, this, word);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        goalRV.setHasFixedSize(true);

        goalRV.setLayoutManager(manager);

        goalRV.setAdapter(adapterSearch);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("goal_all", null);

        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();

        goalArrayList = gson.fromJson(json, type);

        if (goalArrayList == null) {
            goalArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(goalArrayList);
        editor.putString("goal_all", json);

        editor.apply();

    }
}