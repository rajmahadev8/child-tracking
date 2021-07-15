package com.infinity.childtracking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.childtracking.R;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CallDetailAdapter extends ArrayAdapter<CALL> {

    Context context;
    ArrayList<CALL> call_log;

    public CallDetailAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CALL> objects) {
        super(context, resource, objects);
        this.context = context;
        this.call_log = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.call_detail_model,null,true);

        TextView tvDatetime,tvDuration,tvType;

        tvDatetime = row.findViewById(R.id.call_detail_datetime);
        tvDuration = row.findViewById(R.id.call_detail_duration);
        tvType = row.findViewById(R.id.call_detail_type);

        tvDatetime.setText(call_log.get(position).getDatetime());
        tvDuration.setText(call_log.get(position).getDuration());

        switch (call_log.get(position).getType()){
            case 1:
                tvType.setText("Incoming");
                break;
            case 2:
                tvType.setText("Outgoing");
                break;
            case 3:
                tvType.setText("Missed");
                break;
            case 4:
                tvType.setText("Voice Mail");
                break;
            case 5:
                tvType.setText("Rejected");
                break;
            case 6:
                tvType.setText("Blocked");
                break;
            case 7:
                tvType.setText("Auto missed");
                break;
        }
        long seconds = Long.parseLong(call_log.get(position).getDuration());
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minute = TimeUnit.SECONDS.toMinutes(seconds);
        seconds = TimeUnit.SECONDS.toSeconds(seconds);
        tvDuration.setText(hours+":"+minute+":"+seconds);
        return row;
    }
}
