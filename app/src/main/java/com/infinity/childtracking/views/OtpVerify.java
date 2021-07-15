package com.infinity.childtracking.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.User;
import com.infinity.childtracking.util.GenericTextWatcher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpVerify extends AppCompatActivity {

    Button otpBtn;
    ArrayList<EditText> otp = new ArrayList<>();
    RetroAPI retroAPI;
    Retrofit retrofit;
    String trueOtp;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        sendOtp();

        otpBtn = findViewById(R.id.otp_verify_btn);
        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
        otp.add(findViewById(R.id.ed1));
        otp.add(findViewById(R.id.ed2));
        otp.add(findViewById(R.id.ed3));
        otp.add(findViewById(R.id.ed4));
        cursormovable();
    }

    private void verifyOtp() {
        String otpStr ="";
        for(EditText et : otp){
            otpStr += et.getText().toString().trim();
        }
        if(otpStr.equals(trueOtp)){
            //verifired
            String finalUser = DataBank.curUser.getUsertype();
            if(finalUser.equalsIgnoreCase("Child")) {
                Intent intent = new Intent(this, Child.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, Parent.class);
                startActivity(intent);
                finish();
            }
            signup();
        }
        else{
            Toast.makeText(this,"Wrong Otp Entered",Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void signup() {
        pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

        Call<APIResponse> res =  retroAPI.signup(DataBank.curUser);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200){

                    if(pref == null)
                        pref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                    editor = pref.edit();
                    editor.putString("Username",DataBank.curUser.getUsername());
                    editor.putString("UserType", DataBank.curUser.getUsertype());
                    editor.putString("childUser",DataBank.curUser.getChild_parent());
                    editor.commit();
                }
                if (response.code() == 400){
                    Toast.makeText(OtpVerify.this,"Fill All Details",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private void sendOtp(){

        Call<String> res =  retroAPI.sendOtp(DataBank.curUser);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200) {
                    trueOtp = response.body();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    void cursormovable(){
        for(EditText et:otp){
            et.addTextChangedListener(new GenericTextWatcher(et,otp));
        }
    }
}
