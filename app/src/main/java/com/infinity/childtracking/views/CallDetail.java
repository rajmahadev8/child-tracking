package com.infinity.childtracking.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallDetailAdapter;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallDetail extends AppCompatActivity {

    Button backbtn;
    TextView tvMobileno;
    ListView lvCalls;

    Bundle bundle;

    String mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_details);

        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallDetail.this, Parent.class));
                finish();
            }
        });
        bundle = getIntent().getExtras();
        mobile_no = bundle.getString("selected");

        tvMobileno = findViewById(R.id.call_detail_MobileNO);

        ArrayList<CALL> calls = new ArrayList<CALL>();
        try {
            for (int i = 0; i < DataBank.call_log.size(); i++) {
                if (DataBank.call_log.get(i).getNumber().equals(mobile_no)) {
                    calls.add(DataBank.call_log.get(i));
                }
            }
            tvMobileno.setText(calls.get(0).getName());
            Log.d("TAG",String.valueOf(calls.size()));
        }catch (Exception e){
            Log.d("CALLDETAIL",e.getLocalizedMessage());
        }
        lvCalls = findViewById(R.id.call_detail_lv);
        CallDetailAdapter adapter = new CallDetailAdapter(this,R.layout.call_detail_model,calls);
        lvCalls.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CallDetail.this,Parent.class));
        finish();
    }
}
