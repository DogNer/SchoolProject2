package com.example.schoolproject.Screen;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.Adapter.AchieveAdapter;
import com.example.schoolproject.AddActivity;
import com.example.schoolproject.Modal.GoalModal;
import com.example.schoolproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    ImageView btnAdd;
    AppCompatButton btnAddActivity;
    TextView curTime, curDay, cntGoal, nameGoal;
    CalendarView calendar;
    private AchieveAdapter adapter;
    private ArrayList<GoalModal> goalArrayList, arrayListAll;
    private RecyclerView goalRV;

    LinearLayout achieveHavnt;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        curTime = view.findViewById(R.id.time_str);
        curDay = view.findViewById(R.id.day_and_month);
        goalRV = view.findViewById(R.id.recyclerView);
        achieveHavnt = view.findViewById(R.id.havt_achieve);
        btnAdd = view.findViewById(R.id.btn_add);
        btnAddActivity = view.findViewById(R.id.go_to_achieveActivity);
        cntGoal = view.findViewById(R.id.cntAch);

        loadData(view.getContext());
        buildRecyclerView(view.getContext());

        final Handler timeHandler = new Handler(getMainLooper());
        final SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("hh:mm", Locale.UK);
        final SimpleDateFormat simpleDateFormatday =  new SimpleDateFormat("EEEE, dd MMM", Locale.UK);

        timeHandler.post(new Runnable() {
            @Override
            public void run() {
                curTime.setText(simpleDateFormat.format(new Date()));
                timeHandler.postDelayed(this, 1000);
                uploadAchieve();
                cntGoal.setText(String.valueOf(countDoneAch()) + '/' + String.valueOf(goalArrayList.size()));
            }
        });

        curDay.setText(simpleDateFormatday.format(new Date()));

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private int countDoneAch(){
        int cnt = 0;
        for (int i = 0; i < goalArrayList.size(); ++i){
            GoalModal modal = goalArrayList.get(i);
            if (modal.isDoneAch()) cnt++;
        }
        return cnt;
    }

    private void loadData(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

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

    private void buildRecyclerView(Context context) {
        adapter = new AchieveAdapter(goalArrayList, context);
        new ItemTouchHelper(itemTouch).attachToRecyclerView(goalRV);

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        goalRV.setHasFixedSize(true);

        goalRV.setLayoutManager(manager);

        goalRV.setAdapter(adapter);
    }

    private void saveData(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(goalArrayList);
        String json_all = gson.toJson(arrayListAll);

        editor.putString("goal", json);
        editor.putString("goal_all", json_all);

        editor.apply();

    }

    private void uploadAchieve(){
        if (goalArrayList.size() != 0) {
            achieveHavnt.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
        else {
            achieveHavnt.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
        }
    }

    ItemTouchHelper.SimpleCallback itemTouch =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    deleteItem(viewHolder.getAdapterPosition());
                    goalArrayList.remove(viewHolder.getAdapterPosition());

                    adapter.notifyDataSetChanged();
                    saveData(getContext());
                    loadData(getContext());
                }
            };

    public void deleteItem(int pos){
        for (int i = 0; i < arrayListAll.size(); ++i) {
            if (arrayListAll.get(i).getGaol().equals(goalArrayList.get(pos).getGaol()))
                arrayListAll.remove(i);
        }
        saveData(getContext());
    }

}