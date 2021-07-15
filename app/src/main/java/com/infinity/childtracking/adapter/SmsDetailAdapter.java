package com.infinity.childtracking.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.childtracking.R;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.SMS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SmsDetailAdapter extends ArrayAdapter<SMS> {

    Context context;
    ArrayList<SMS> sms_log;
    View row;

    public SmsDetailAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SMS> objects) {
        super(context, resource, objects);
        this.context = context;
        this.sms_log = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.sms_detail_model,null,true);
        TextView tvDatetime;
        tvDatetime = row.findViewById(R.id.sms_detail_datetime);

        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
        long seconds = Long.parseLong(DataBank.sms_log.get(position).getDate());
        tvDatetime.setText(sm.format(new Date(seconds)));

        switch (Integer.valueOf(sms_log.get(position).getType())){
            case 1:
                toTheLeft(position);
                break;
            case 2:
                toTheRight(position);
                break;
        }
        return row;
    }

    private void toTheLeft(int position) {
        TextView tvBody,tvType;

        switchVisibility(true);

        tvBody = row.findViewById(R.id.sms_detail_body);
        tvType = row.findViewById(R.id.sms_detail_type);

        tvBody.setText(sms_log.get(position).getBody());
        tvType.setText("Arrived");
    }

    private void toTheRight(int position) {
        TextView tvBody,tvType;
        switchVisibility(false);

        TextView tvDatetime;
        tvDatetime = row.findViewById(R.id.sms_detail_datetime);
        tvDatetime.setGravity(Gravity.END);

        tvBody = row.findViewById(R.id.sms_detail_body2);
        tvType = row.findViewById(R.id.sms_detail_type2);

        tvBody.setText(sms_log.get(position).getBody());
        tvType.setText("Sent");
    }


    private void switchVisibility(boolean isLeft) {
        LinearLayout left = row.findViewById(R.id.toTheLeft),right = row.findViewById(R.id.toTheRight);

        if(isLeft) {
            right.setVisibility(View.GONE);
            left.setVisibility(View.VISIBLE);
        }else {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.GONE);
        }
    }
}
