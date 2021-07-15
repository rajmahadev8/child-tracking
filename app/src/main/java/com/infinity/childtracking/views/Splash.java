package com.infinity.childtracking.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinity.childtracking.MainActivity;
import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.model.SMS;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity {
    RelativeLayout layout;

    Animation topAnim,bottomAnim;
    ImageView logo;
    TextView name;

    Retrofit retrofit;
    RetroAPI retroAPI;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
       // getAllData();

        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        layout = findViewById(R.id.loder);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },2500);
            }
        },1500);
    }


}