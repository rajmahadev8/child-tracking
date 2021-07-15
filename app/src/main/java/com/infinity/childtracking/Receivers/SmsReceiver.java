package com.infinity.childtracking.Receivers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.model.SMS;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SmsReceiver extends BroadcastReceiver {
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] sms;
        SMS receivedSms;
        String format = bundle.getString("format");

        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if(pdus != null){
            boolean isversionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

            sms = new SmsMessage[pdus.length];
            for (int i=0;i<sms.length;i++){

                if(isversionM){
                    sms[i] = SmsMessage.createFromPdu((byte[])pdus[i],format);
                }else {
                    sms[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                String address = sms[i].getOriginatingAddress();
                String body = sms[i].getMessageBody();

                SimpleDateFormat sm = new SimpleDateFormat("dd:MM:yy hh:mm:ss a");
                long sec = sms[i].getTimestampMillis();
                String date = sm.format(new Date(sec));
                String type="1";
                boolean read = false;
                String id = String.valueOf(sms[i].getProtocolIdentifier());
                receivedSms = new SMS(id,address,body,date,type,read);
                sendReq(receivedSms);
            }
        }
    }

    private void sendReq(SMS receivedSms) {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        Call<APIResponse> res = retroAPI.sendSms(receivedSms);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("SMSSENT","success");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("SMSSENT","failure "+ t.getMessage());
            }
        });
    }
}
