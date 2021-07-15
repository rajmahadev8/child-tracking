package com.infinity.childtracking.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildNotification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ChildNotify extends NotificationListenerService {

    Context context;
    static CstmListner mListner;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.d("Notify","Service Start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            String pack = sbn.getPackageName();
            Bundle extra = sbn.getNotification().extras;
            String title = extra.getString("android.title");
            String text = extra.getCharSequence("android.text").toString();

            Log.d("Notify", pack + " "  + title + " " + text);

            if (DataBank.curUser == null)
                Log.d("Notify","null");
            ChildNotification notification = new ChildNotification(pack,title,text,DataBank.curUser.getUsername());
            DataBank.notifications.add(notification);
            send_req(notification);
            if (mListner != null)
                mListner.setValue("one: " + pack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void send_req(ChildNotification notification) {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI api = retrofit.create(RetroAPI.class);

        Call<APIResponse> res = api.insertNotify(notification);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("Notify","Success");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("Notify","Failure");
            }
        });
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("Notify","Notification Removed");
        if(mListner != null)
            mListner.setValue("One :" +sbn.getPackageName());
    }

    public void setListner(CstmListner mListner){
        this.mListner = mListner;
    }
}
