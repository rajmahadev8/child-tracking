package com.infinity.childtracking.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.infinity.childtracking.MainActivity;
import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.SetupPageAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.fragments.child.SetupCallSms;
import com.infinity.childtracking.fragments.child.SetupLocation;
import com.infinity.childtracking.fragments.child.SetupNotification;
import com.infinity.childtracking.fragments.child.SetupUsage;
import com.infinity.childtracking.model.User;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class Child extends AppCompatActivity implements View.OnClickListener {

    Button prevBtn,nextBtn;
    ViewPager viewPager;
    TabLayout tab;

    SharedPreferences pref;

    Retrofit retrofit;
    RetroAPI retroAPI;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        retrofit = RetroClient.getRetrofit();
        retroAPI =  retrofit.create(RetroAPI.class);

        pref = getSharedPreferences("Login", Context.MODE_PRIVATE);
        isLoggedin();
        fetchFrompref();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SetupCallSms());
        fragments.add(new SetupLocation());
        fragments.add(new SetupNotification());
        fragments.add(new SetupUsage());

        viewPager = findViewById(R.id.c_viewPager);
        SetupPageAdapter adapter = new SetupPageAdapter(getSupportFragmentManager(),
                fragments,this);
        viewPager.setAdapter(adapter);
        tab = findViewById(R.id.nav_dots);
        tab.setupWithViewPager(viewPager);

        prevBtn = findViewById(R.id.prev);
        nextBtn = findViewById(R.id.next);
        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    private void isLoggedin() {

        if (pref == null)
            pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (!pref.contains("Username")) {
            Intent intent = new Intent(Child.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void fetchFrompref() {
        if(pref == null)
            pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if(DataBank.curUser == null)
        {
            DataBank.curUser = new User(pref.getString("Username","")
                    ,pref.getString("UserType","")
                    ,pref.getString("childUser",""));
        }
    }

    @Override
    public void onClick(View v) {
        if(v == prevBtn){
            viewPager.setCurrentItem(getItem(-1));
        }
        if(v==nextBtn){
            viewPager.setCurrentItem(getItem(1));
            if (getItem(1) >= 3){
                startActivity(new Intent(this, ChildHideIcon.class));
                finish();
            }
        }
    }

    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
}