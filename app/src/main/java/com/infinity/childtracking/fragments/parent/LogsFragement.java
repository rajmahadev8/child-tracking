package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallLogAdapter;
import com.infinity.childtracking.adapter.ParentTabAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.CallDetail;
import com.infinity.childtracking.views.Parent;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogsFragement extends FragmentActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_main);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CalllogFragement(R.layout.call_log,getResources().getString(R.string.call_log),0));
        fragments.add(new MsglogFragment(R.layout.msg_log,getResources().getString(R.string.msg_log),1));
        fragments.add(new NotifylogFragement(R.layout.notify_log,getString(R.string.notify_log),2));
        fragments.add(new LocationlogFragement(R.layout.location_log,getString(R.string.location_log),3));

        viewPager = findViewById(R.id.p_viewPager);
        ParentTabAdapter adapter = new ParentTabAdapter(getSupportFragmentManager(),fragments,this);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.p_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Parent.class));
        finish();
    }
}