package com.infinity.childtracking.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import com.infinity.childtracking.MainActivity;
import com.infinity.childtracking.R;

public class ChildHideIcon extends AppCompatActivity {

    Button btn,logout;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childicon);

        pref = getSharedPreferences("Login", Context.MODE_PRIVATE);
        btn = findViewById(R.id.child_hideIcon);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideIcon();
            }
        });

        logout = findViewById(R.id.child_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void HideIcon() {
        PackageManager manager = getPackageManager();
        ComponentName name = new ComponentName(this,Splash.class);
        manager.setComponentEnabledSetting(name,PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        ,PackageManager.DONT_KILL_APP);
    }


    private void logout(){

        if (pref == null)
            pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if(pref.contains("Username")){
            editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(ChildHideIcon.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
