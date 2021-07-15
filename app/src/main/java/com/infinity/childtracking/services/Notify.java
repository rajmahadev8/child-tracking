package com.infinity.childtracking.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.Token;
import com.infinity.childtracking.views.CallDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Notify extends FirebaseMessagingService {

    String title,body;
    String ch_id = "1";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TOKEN",s);
        DataBank.curToken = new Token(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try{
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
        }catch (Exception e){
            e.printStackTrace();
        }
        notification();
    }

    private void notification(){
        createNotificationChannel();

        Intent i = new Intent(this, CallDetail.class);
        i.putExtra("selected",body.substring(10));
        Log.d("TOKEN",body.substring(10));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 1,i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ch_id)
                .setSmallIcon(R.drawable.child1)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingintent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(404, builder.build());

    }
    private void createNotificationChannel() {
        //Only for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my notification";
            String description = "notify";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ch_id, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
