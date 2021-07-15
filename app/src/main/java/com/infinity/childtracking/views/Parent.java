package com.infinity.childtracking.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.infinity.childtracking.MainActivity;
import com.infinity.childtracking.R;
import com.infinity.childtracking.Receivers.CallReceiver;
import com.infinity.childtracking.Receivers.SmsReceiver;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.fragments.parent.CalllogFragement;
import com.infinity.childtracking.fragments.parent.ChildrenFragement;
import com.infinity.childtracking.fragments.parent.DashboardFragement;
import com.infinity.childtracking.fragments.parent.LocationFragmentActivity;
import com.infinity.childtracking.fragments.parent.LocationlogFragement;
import com.infinity.childtracking.fragments.parent.MsglogFragment;
import com.infinity.childtracking.fragments.parent.NotifylogFragement;
import com.infinity.childtracking.fragments.parent.LogsFragement;
import com.infinity.childtracking.fragments.parent.UsageFragement;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.model.SMS;
import com.infinity.childtracking.model.Token;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.model.User;
import com.infinity.childtracking.services.ChildNotify;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Parent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG_FRAGMENT="fragments";

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navView;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Retrofit retrofit;
    RetroAPI retroAPI;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        pref = getSharedPreferences("Login", MODE_PRIVATE);
        isLoggedin();
        fetchFrompref();

        drawer = findViewById(R.id.p_drawer);
        toolbar = (Toolbar) findViewById(R.id.p_toolbar);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View v = navView.getHeaderView(0);
        TextView tv = v.findViewById(R.id.p_username);
        tv.setText("Welcome "+ DataBank.curUser.getUsername());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(404);

        deRegisterReceiver();
        insertToken();
        getAllData();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashboardFragement()).commit();
    }

    private void isLoggedin() {

        if (pref == null)
            pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (!pref.contains("Username")) {
            Intent intent = new Intent(Parent.this, MainActivity.class);
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

    private void logout() {

        if (pref == null)
            pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (pref.contains("Username")) {
            editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Parent.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void deRegisterReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(new CallReceiver());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(new SmsReceiver());
        stopService(new Intent(this, ChildNotify.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.p_menu_logs:
                intent = new Intent(this,LogsFragement.class);
                startActivity(intent);
                finish();
                break;
            case R.id.p_menu_call:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalllogFragement(R.layout.call_log,getString(R.string.call_log),0),TAG_FRAGMENT)
                        .addToBackStack("call").commit();
                break;
            case R.id.p_menu_sms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MsglogFragment(R.layout.msg_log,getString(R.string.msg_log),1),TAG_FRAGMENT).commit();
                break;
            case R.id.p_menu_notify:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotifylogFragement(R.layout.notify_log,getString(R.string.notify_log),2),TAG_FRAGMENT).commit();
                break;
            case R.id.p_menu_location_log:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LocationlogFragement(R.layout.location_log,getString(R.string.location_log),3),TAG_FRAGMENT).commit();
                break;
            case R.id.p_menu_location:
                intent = new Intent(this, LocationFragmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.p_menu_child:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChildrenFragement()).commit();
                break;
            case R.id.p_menu_prevent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UsageFragement()).commit();
                break;
            case R.id.p_menu_logout:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        boolean isDashboard = false;
        for(Fragment f:getSupportFragmentManager().getFragments()){
            if(f instanceof DashboardFragement){
                isDashboard = true;
                break;
            }
        }
        if(!isDashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragement()).commit();
        }
    }

    private void insertToken() {
        if(DataBank.curToken != null) {
            Retrofit retrofit = RetroClient.getRetrofit();
            RetroAPI retroAPI = retrofit.create(RetroAPI.class);

            DataBank.curToken = new Token(DataBank.curToken.getToken(),DataBank.curUser.getUsername());

            Call<APIResponse> res = retroAPI.insertToken(DataBank.curToken);
            res.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    Log.d("TOKEN", "Success ");
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Log.d("TOKEN", "Success " + t.getMessage());
                }
            });
        }
    }

    private void getAllData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCallLog();
            }
        }).start();
    }

    private void getCallLog() {
        if (DataBank.call_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Log.d("HD",childUser + "no");
            Log.d("HD",retroAPI != null?"notnull":"null");
            Call<ArrayList<CALL>> res = retroAPI.getCall(childUser);
            res.enqueue(new Callback<ArrayList<CALL>>() {
                @Override
                public void onResponse(Call<ArrayList<CALL>> call, Response<ArrayList<CALL>> response) {
                    if (response.code() == 200) {
                        DataBank.call_log = response.body();
                        getSmsLog();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CALL>> call, Throwable t){
                }
            });
        }
    }

    private void getSmsLog() {
        if (DataBank.sms_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Call<ArrayList<SMS>> res = retroAPI.getSms(childUser);
            res.enqueue(new Callback<ArrayList<SMS>>() {
                @Override
                public void onResponse(Call<ArrayList<SMS>> call, Response<ArrayList<SMS>> response) {
                    if (response.code() == 200) {
                        DataBank.sms_log = response.body();
                        getNotifyLog();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SMS>> call, Throwable t) {

                }
            });
        }
    }


    private void getNotifyLog() {
        if (DataBank.notifications.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Call<ArrayList<ChildNotification>> res = retroAPI.getNotify(childUser);
            res.enqueue(new Callback<ArrayList<ChildNotification>>() {
                @Override
                public void onResponse(Call<ArrayList<ChildNotification>> call, Response<ArrayList<ChildNotification>> response) {
                    if (response.code() == 200) {
                        DataBank.notifications = response.body();
                        getLocationLog();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChildNotification>> call, Throwable t) {
                }
            });
        }
    }

    private void getLocationLog() {

        if (DataBank.location_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();

            Call<ChildLocation> res = retroAPI.getLocation(childUser);
            res.enqueue(new Callback<ChildLocation>() {
                @Override
                public void onResponse(Call<ChildLocation> call, Response<ChildLocation> response) {
                    DataBank.location_log.add(response.body());
                    getChild();
                }

                @Override
                public void onFailure(Call<ChildLocation> call, Throwable t) {

                }
            });
        }
    }
    private String[] getChild() {
        if (DataBank.child_users != null) {
            Retrofit retrofit = RetroClient.getRetrofit();
            RetroAPI retroAPI = retrofit.create(RetroAPI.class);

            Call<ArrayList<String>> res = retroAPI.getChild(DataBank.curUser.getUsername());
            res.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.code() == 200) {
                        DataBank.child_users = response.body();
                    }
                    getUsage();
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                }
            });
        }
        return DataBank.child_users.toArray(new String[0]);
    }

    private void getUsage() {
        if (DataBank.usages_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Call<ArrayList<USAGE>> res = retroAPI.getUsage(childUser);
            res.enqueue(new Callback<ArrayList<USAGE>>() {
                @Override
                public void onResponse(Call<ArrayList<USAGE>> call, Response<ArrayList<USAGE>> response) {
                    if(response.code() == 200){
                        DataBank.usages_log = response.body();
                        Log.d("HIIIIdata",DataBank.usages_log.get(0)+"");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<USAGE>> call, Throwable t) {

                }
            });
        }
    }

}
