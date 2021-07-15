package com.infinity.childtracking.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallDetailAdapter;
import com.infinity.childtracking.adapter.NotifyDetailAdapter;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.ChildNotification;

import java.util.ArrayList;

public class NotifyDetail extends AppCompatActivity {

    Button backbtn;
    TextView tvpkg;
    ListView lvNotify;
    Bundle bundle;

    String pkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_details);

        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifyDetail.this, Parent.class));
                finish();
            }
        });
        bundle = getIntent().getExtras();
        pkg = bundle.getString("selected");

        tvpkg = findViewById(R.id.notify_detail_title);
        tvpkg.setText(pkg);


        ArrayList<ChildNotification> notify_log = new ArrayList<ChildNotification>();
        try {
            for (int i = 0; i < DataBank.notifications.size(); i++) {
                if (DataBank.notifications.get(i).getPkg().contains(pkg)) {
                    notify_log.add(DataBank.notifications.get(i));
                }
            }
        }catch (Exception e){
            Log.d("NotifyDETAIL",e.getLocalizedMessage());
        }
        lvNotify = findViewById(R.id.notify_detail_lv);
        NotifyDetailAdapter adapter = new NotifyDetailAdapter(this,R.layout.notify_detail_model,notify_log);
        lvNotify.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotifyDetail.this,Parent.class));
        finish();
    }
}
