package com.infinity.childtracking.fragments.child;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.services.ChildNotify;
import com.infinity.childtracking.services.CstmListner;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetupUsage extends Fragment {
    Button setBtn;
    Retrofit retrofit;
    RetroAPI retroAPI;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_usage,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        setBtn = view.findViewById(R.id.set_usage);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Give Usage Access",Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                getData();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getData() {
        UsageStatsManager usageStatsManager = (UsageStatsManager)getActivity().getSystemService(
                Context.USAGE_STATS_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        long start = cal.getTimeInMillis();
        long end = System.currentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_MONTHLY,
                start,
                end
        );
        for(UsageStats s: stats){
            USAGE u = new USAGE(s.getPackageName()
                    ,s.getLastTimeStamp()+""
                    ,s.getFirstTimeStamp()+""
                ,DataBank.curUser.getUsername());
            DataBank.usages_log.add(u);
        }
        sendUsage();
    }

    private void sendUsage() {
        Call<APIResponse> res =  retroAPI.insertUsage(DataBank.usages_log);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200){
                    Log.d("USAGE",response.body().isRight() + "");
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}
