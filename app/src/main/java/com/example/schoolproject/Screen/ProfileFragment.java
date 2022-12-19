package com.example.schoolproject.Screen;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolproject.ActForScreen;
import com.example.schoolproject.Adapter.AchieveAdapter;
import com.example.schoolproject.Draw.DrawRectangle;
import com.example.schoolproject.Modal.GoalModal;
import com.example.schoolproject.Personal_clock;
import com.example.schoolproject.R;
import com.example.schoolproject.ReminderBroadcast;
import com.example.schoolproject.TimePickerFragment;
import com.example.schoolproject.Windows.EditAlarm;
import com.example.schoolproject.Windows.RegisterWindow;
import com.example.schoolproject.Windows.SearchWindow;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{

    TextView userName, AlarmTime;
    private ArrayList<GoalModal> goalArrayList;
    private Button btnChoose;

    CardView cardMark;
    CardView btnSearch, btnInfo;
    private AppCompatButton btnNo, btnYes, btnChName;
    ImageView btnClose, btnEditName, btnEditTime;

    TextView textHowUse;
    LinearLayout checkTHowUse, disAdd, checkTAdd;
    ImageView btnBack, btnBackName;
    EditText nameCh;

    DrawRectangle drawRec;
    LinearLayout surfaceView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //createNotificationChannel();
        userName = view.findViewById(R.id.user_name);

        AlarmTime = view.findViewById(R.id.textAlarm);
        cardMark = view.findViewById(R.id.card_mark);
        btnChoose = view.findViewById(R.id.btn_mark);
        btnSearch = view.findViewById(R.id.btn_search);
        btnInfo = view.findViewById(R.id.btn_annotation);
        btnEditName = view.findViewById(R.id.edit_name);
        btnEditTime = view.findViewById(R.id.edit_time);

        Paint paint = new Paint();
        surfaceView = view.findViewById(R.id.surface);
        Bitmap bg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bg);
        paint.setColor(Color.RED);
        canvas.drawRect(50, 50, 200, 200, paint);
        surfaceView.setBackground(new BitmapDrawable(bg));


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editer = sp.edit();

        userName.setText(sp.getString("user_name", "error"));

        AlarmTime.setText(String.format("%02d", sp.getInt("hour", 0)) + ":" + String.format("%02d", sp.getInt("minute", 0)));

        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);


        if (System.currentTimeMillis() >= sp.getLong("timeAlarm", 0) && !sp.getBoolean("mark", false)){
            cardMark.setVisibility(View.VISIBLE);
        }
        else cardMark.setVisibility(View.GONE);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogIsDone(getContext());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchWindow.class);
                startActivity(intent);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogInfo(view.getContext());
            }
        });

        btnEditTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Personal_clock.class);
                startActivity(intent);
            }
        });

        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditName(getContext());
            }
        });


        return view;
    }

    private void dialogEditName(Context context) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dg_edit_name);

        btnBackName = dialog.findViewById(R.id.ic_back);
        nameCh = dialog.findViewById(R.id.str_name);
        btnChName = dialog.findViewById(R.id.btn_name_ch);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editer = sp.edit();

        nameCh.setText(sp.getString("user_name", "error"));

        btnChName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameCh.getText().toString();
                if (!name.isEmpty()){
                    editer.putString("user_name", name);
                    editer.commit();
                    userName.setText(name);

                    dialog.dismiss();
                }
            }
        });

        btnBackName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogIsDone(Context context) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dg_mark_achieve);

        btnNo = dialog.findViewById(R.id.btn_no);
        btnYes = dialog.findViewById(R.id.btn_yes);
        btnClose = dialog.findViewById(R.id.close_btn);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataAll();
                deleteArray();
                saveData();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editer = sp.edit();

                editer.putBoolean("mark", true);
                editer.apply();

                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogInfo(Context context) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_info);

        checkTHowUse = dialog.findViewById(R.id.checkHowUse);
        textHowUse = dialog.findViewById(R.id.textHowUse);
        checkTAdd = dialog.findViewById(R.id.checkHowAdd);
        disAdd = dialog.findViewById(R.id.descAdd);
        btnBack = dialog.findViewById(R.id.ic_back);

        checkTHowUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textHowUse.getVisibility() == View.GONE)
                    textHowUse.setVisibility(View.VISIBLE);
                else textHowUse.setVisibility(View.GONE);
            }
        });

        checkTAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (disAdd.getVisibility() == View.GONE)
                    disAdd.setVisibility(View.VISIBLE);
                else disAdd.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void deleteArray(){
        for (int i = 0; i < goalArrayList.size(); ++i){
            goalArrayList.remove(i);
            --i;
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(goalArrayList);
        editor.putString("goal", json);

        editor.apply();
    }

    private void loadDataAll() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Gson gson = new Gson();

        String json = sharedPreferences.getString("goal", null);

        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();

        goalArrayList = gson.fromJson(json, type);

        if (goalArrayList == null) {
            goalArrayList = new ArrayList<>();
        }
    }
}
