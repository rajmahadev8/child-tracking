package com.infinity.childtracking.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.infinity.childtracking.R;

import java.util.ArrayList;

public class ParentTabAdapter extends FragmentPagerAdapter {
    
    public static final int[] TAB_TITLE = new int[]{R.string.call_log,R.string.msg_log,R.string.notify_log,R.string.location_log};
    private Context context;
    public static final int NUM_TAB = 4;
    ArrayList<Fragment> fragments = new ArrayList<>();

    public ParentTabAdapter(@NonNull FragmentManager fm,ArrayList<Fragment> fragments,Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return NUM_TAB;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLE[position]);
    }
}
