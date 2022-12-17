package com.example.schoolproject.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.Modal.GoalModal;
import com.example.schoolproject.R;
import com.example.schoolproject.Screen.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AchieveAdapter extends RecyclerView.Adapter<AchieveAdapter.ViewHolder> {

    private ArrayList<GoalModal> achieveAdapterList, arrayListAll;
    private Context context;
    TextView posView;
    EditText editAchieve;
    AppCompatButton btnCh, btnNo, btnYes;
    String editText;
    ImageView btnClose;

    @NonNull
    @Override
    public AchieveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achieve, parent, false);
        return new AchieveAdapter.ViewHolder(view);
    }

    public AchieveAdapter(ArrayList<GoalModal> achieveAdapterList, Context context) {
        this.context = context;
        this.achieveAdapterList = achieveAdapterList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoalModal modal = achieveAdapterList.get(position);
        holder.goalName.setText(modal.getGaol());

        if(!modal.isDoneAch()) {
            holder.isDone.setVisibility(View.GONE);
        }

        int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogIsDone(holder, modal, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return achieveAdapterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView goalName;
        private ImageView isDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            goalName =  itemView.findViewById(R.id.nameGoal);
            isDone = itemView.findViewById(R.id.isDone);

        }
    }

    private void showDialogIsDone(ViewHolder holder, GoalModal modal, int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dg_is_done);

        btnNo = dialog.findViewById(R.id.btn_no);
        btnYes = dialog.findViewById(R.id.btn_yes);
        btnClose = dialog.findViewById(R.id.close_btn);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                holder.isDone.setVisibility(View.VISIBLE);
                modal.setDoneAch(true);
                findGoalInAll(modal, true);
                saveData();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                holder.isDone.setVisibility(View.GONE);
                modal.setDoneAch(false);
                findGoalInAll(modal, false);
                saveData();
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

    public void findGoalInAll(GoalModal modal, boolean isDone){
        for (int i = 0; i < arrayListAll.size(); ++i){
            if (arrayListAll.get(i).getGaol().equals(modal.getGaol()))
                arrayListAll.get(i).setDoneAch(isDone);
        }
    }
    private void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(achieveAdapterList);
        String json_all = gson.toJson(arrayListAll);

        editor.putString("goal", json);
        editor.putString("goal_all", json_all);

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();

        String json_all = sharedPreferences.getString("goal_all", null);

        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();

        arrayListAll = gson.fromJson(json_all, type);
        if (arrayListAll == null) {
            arrayListAll = new ArrayList<>();
        }
    }
}
