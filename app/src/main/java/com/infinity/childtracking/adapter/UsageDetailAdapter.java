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
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.model.USAGE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UsageDetailAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> usage_log = new ArrayList<>();

    public UsageDetailAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.usage_log = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.usage_detail_model,null,true);

        TextView tvText;

        tvText = row.findViewById(R.id.usage_detail_lasttime);
        long seconds = Long.parseLong(usage_log.get(position));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String str =  format.format(new Date(seconds));
        tvText.setText(str);
        return row;
    }
}
