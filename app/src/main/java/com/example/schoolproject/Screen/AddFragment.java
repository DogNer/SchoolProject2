package com.example.schoolproject.Screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schoolproject.Adapter.AchieveAdapter;
import com.example.schoolproject.Modal.GoalModal;
import com.example.schoolproject.R;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    EditText goal_edit;
    AppCompatButton btnAdd;
    private AchieveAdapter adapter;
    private ArrayList<GoalModal> goalArrayList = new ArrayList<GoalModal>();
    private RecyclerView courseRV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        goal_edit = view.findViewById(R.id.str_achieve);
        btnAdd = view.findViewById(R.id.btn_add);
        courseRV = view.findViewById(R.id.recyclerView);

        loadData(view.getContext());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal = goal_edit.getText().toString();
                if (!goal.isEmpty()){
                    goalArrayList.add(new GoalModal(goal, false));
                    saveData(view.getContext());

                    Toast.makeText(view.getContext(), goal, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



    private void saveData(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(goalArrayList);
        editor.putString("goal", json);

        editor.apply();

    }

    private void loadData(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("goal", null);

        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();

        goalArrayList = gson.fromJson(json, type);

        if (goalArrayList == null) {
            goalArrayList = new ArrayList<>();
        }
    }

}