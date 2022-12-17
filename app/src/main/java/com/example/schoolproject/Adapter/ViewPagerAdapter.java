package com.example.schoolproject.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.schoolproject.Screen.HomeFragment;
import com.example.schoolproject.Screen.ProfileFragment;
import com.example.schoolproject.databinding.FragmentProfileBinding;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
