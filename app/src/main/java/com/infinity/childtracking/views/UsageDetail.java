package com.infinity.childtracking.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.UsageDetailAdapter;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.USAGE;

import java.util.ArrayList;

public class UsageDetail extends AppCompatActivity {

    Button backbtn;
    TextView tvpkg;
    ListView lvUsage;
    Bundle bundle;

    String pkg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usage_detail);


        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsageDetail.this, Parent.class));
                finish();
            }
        });
        bundle = getIntent().getExtras();
        pkg = bundle.getString("selected");

        tvpkg = findViewById(R.id.usage_detail_title);
        tvpkg.setText(pkg);

        ArrayList<String> usages_log = new ArrayList<String>();
        for (int i = 0; i < DataBank.usages_log.size(); i++) {
            USAGE u = DataBank.usages_log.get(i);
            if (u.getPkg().contains(pkg)) {
                usages_log.add(u.getLasttime());
            }

        }

        lvUsage = findViewById(R.id.usage_detail_lv);
        UsageDetailAdapter adapter = new UsageDetailAdapter(this, R.layout.usage_detail_model, usages_log);
        lvUsage.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UsageDetail.this, Parent.class));
        finish();
    }

}
