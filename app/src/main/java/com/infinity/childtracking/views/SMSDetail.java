package com.infinity.childtracking.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallDetailAdapter;
import com.infinity.childtracking.adapter.SmsDetailAdapter;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.SMS;

import java.util.ArrayList;

public class SMSDetail extends AppCompatActivity {

    Button backbtn;
    TextView tvMobileno;
    ListView lvCalls;

    Bundle bundle;

    String mobile_no;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_details);

        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SMSDetail.this, Parent.class));
                finish();
            }
        });
        bundle = getIntent().getExtras();
        mobile_no = bundle.getString("selected");

        tvMobileno = findViewById(R.id.sms_detail_MobileNO);

        ArrayList<SMS> sms = new ArrayList<SMS>();
        try {
            for (int i = 0; i < DataBank.sms_log.size(); i++) {
                if (DataBank.sms_log.get(i).getAddress().equals(mobile_no)) {
                    sms.add(DataBank.sms_log.get(i));
                }
            }
        }catch (Exception e){
            Log.d("SMSDETAIL",e.getLocalizedMessage());
        }

        tvMobileno.setText(sms.get(0).getAddress());

        lvCalls = findViewById(R.id.sms_detail_lv);
        SmsDetailAdapter adapter = new SmsDetailAdapter(this,R.layout.sms_detail_model,sms);
        lvCalls.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SMSDetail.this,Parent.class));
        finish();
    }
}
