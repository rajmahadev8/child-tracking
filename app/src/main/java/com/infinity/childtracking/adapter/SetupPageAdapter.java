package com.infinity.childtracking.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.infinity.childtracking.R;

import java.util.ArrayList;

public class SetupPageAdapter extends FragmentPagerAdapter {

    public static final int ITEM_NUM = 4;
    ArrayList<Fragment> fragments = new ArrayList<>();

    public SetupPageAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return ITEM_NUM;
    }
}
