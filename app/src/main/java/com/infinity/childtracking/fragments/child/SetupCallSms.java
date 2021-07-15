package com.infinity.childtracking.fragments.child;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
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
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.SMS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetupCallSms extends Fragment {
    Button callSmsBtn;

    Retrofit retrofit;
    RetroAPI retroAPI;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_callsms,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = RetroClient.getRetrofit();
        retroAPI =  retrofit.create(RetroAPI.class);

        callSmsBtn = view.findViewById(R.id.set_call_sms);
        callSmsBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getData() {
        if(HavePermission()){
            insertCallLog();
        }else{
            requestPermissions(new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_SMS
            },1024);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean HavePermission() {
        return getActivity().checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public void insertCallLog() {
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor c = getActivity().getContentResolver().query(uri, null, null, null, CallLog.Calls.DATE + " DESC");
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

            CALL call = new CALL(number, name, duration, type,datetime,DataBank.curUser.getUsername());
            DataBank.call_log.add(call);
        }
        Call<APIResponse> res = retroAPI.insertCall(DataBank.call_log);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200){
                    insertSMS();
                    // Toast.makeText(Child.this,response.body().toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private void insertSMS() {
        Uri uri = Uri.parse("content://sms/");
        Cursor c = getActivity().getContentResolver().query(uri,null,null,null, Telephony.Sms.DATE + " DESC");
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(Telephony.Sms._ID));
            String address = c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS));
            String body = c.getString(c.getColumnIndex(Telephony.Sms.BODY));
            boolean read = Boolean.valueOf(c.getString(c.getColumnIndex(Telephony.Sms.READ)));
            String date = c.getString(c.getColumnIndex(Telephony.Sms.DATE));
            String type = c.getString(c.getColumnIndex(Telephony.Sms.TYPE));

            if (!isANumber(address) || isContainSpec(body)) {
                continue;
            }
            SMS newSms = new SMS(id, address, body, date, type, read,DataBank.curUser.getUsername());
            DataBank.sms_log.add(newSms);
        }
        Call<APIResponse> res = retroAPI.insertSMS(DataBank.sms_log);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    Log.d("insertSMS", String.valueOf(response.body().isRight()));
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private boolean isContainSpec(String body) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(body);
        return matcher.find();
    }

    private boolean isANumber(String address) {
        address = address.substring(1);
        for (int i = 0; i < address.length(); i++) {
            if (!Character.isDigit(address.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
