package com.infinity.childtracking.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.infinity.childtracking.R;

import java.util.ArrayList;
import java.util.List;

public class loginPageAdapter extends FragmentPagerAdapter {

    public static final int[] TAB_TITLE = new int[]{R.string.Login,R.string.Signup};
    private final Context mContext;
    public static final int ITEM_NUM = 2;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLE[position]);
    }

    public loginPageAdapter(@NonNull FragmentManager fm,ArrayList<Fragment> fragments, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
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
