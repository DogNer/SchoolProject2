package com.example.schoolproject.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder>{

    private ArrayList<GoalModal> achieveAdapterList;
    private Context context;
    TextView posView;
    EditText editAchieve;
    AppCompatButton btnCh, btnNo, btnYes;
    String editText;
    ImageView btnClose;
    String wordSearch;
    static public Boolean check;
    public int cnt = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achieve, parent, false);
        return new AdapterSearch.ViewHolder(view);
    }

    public AdapterSearch(ArrayList<GoalModal> achieveAdapterList, Context context, String wordS) {
        this.context = context;
        this.achieveAdapterList = achieveAdapterList;
        this.wordSearch = wordS;
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
                String editCh = null;
                showDialogIsDone(holder, modal, pos);
            }
        });

        if (!wordSearch.isEmpty()){
            if (!findWord(modal.getGaol())){
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                holder.goalName.setText(highlightText(modal.getGaol()));
            }
            else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        else holder.itemView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return achieveAdapterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView goalName;
        private ImageView isDone;
        private View items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            goalName =  itemView.findViewById(R.id.nameGoal);
            isDone = itemView.findViewById(R.id.isDone);
        }
    }

    private SpannableString highlightText(String goal){
        if (!findWord(goal)){
            int sPos = goal.indexOf(wordSearch);
            SpannableString ss = new SpannableString(goal);
            while(sPos != -1){
                ss.setSpan(new ForegroundColorSpan(Color.RED), sPos,  sPos + wordSearch.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                sPos = goal.indexOf(wordSearch, sPos + 1);
            }
            return ss;
        }
        return null;
    }

    private void showDialogIsDone(AdapterSearch.ViewHolder holder, GoalModal modal, int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dg_is_done);

        btnNo = dialog.findViewById(R.id.btn_no);
        btnYes = dialog.findViewById(R.id.btn_yes);
        btnClose = dialog.findViewById(R.id.close_btn);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.isDone.setVisibility(View.VISIBLE);
                modal.setDoneAch(true);
                saveData();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.isDone.setVisibility(View.GONE);
                modal.setDoneAch(false);
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

    private void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(achieveAdapterList);
        editor.putString("goal_all", json);

        editor.apply();

    }

    private boolean findWord(String goal){
        if (goal.indexOf(wordSearch) != -1) return false;
        return true;
    }
}
