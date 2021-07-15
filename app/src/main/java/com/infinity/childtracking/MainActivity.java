package com.infinity.childtracking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.infinity.childtracking.adapter.loginPageAdapter;
import com.infinity.childtracking.fragments.loginFragment;
import com.infinity.childtracking.fragments.signupFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new loginFragment(R.layout.activity_login,getResources().getString(R.string.Login),0));
        fragments.add(new signupFragment(R.layout.activity_signup,getResources().getString(R.string.Signup),1));

        ViewPager viewPager = findViewById(R.id.viewPager);
        loginPageAdapter adapter = new loginPageAdapter(getSupportFragmentManager(),fragments,this);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        askForPermit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForPermit() {
        requestPermissions(new String[]{
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE}, 1024);

    }
}