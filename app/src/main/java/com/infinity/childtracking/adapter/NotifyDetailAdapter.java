package com.infinity.childtracking.adapter;

import android.content.Context;
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
import com.infinity.childtracking.model.ChildNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotifyDetailAdapter extends ArrayAdapter<ChildNotification> {

    Context context;
    ArrayList<ChildNotification> notify_log;

    public NotifyDetailAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChildNotification> objects) {
        super(context, resource, objects);
        this.context = context;
        this.notify_log = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.notify_detail_model,null,true);

        TextView tvText,tvTitle;

        tvTitle = row.findViewById(R.id.notify_detail_title);
        tvTitle.setText(notify_log.get(position).getTitle());

        tvText = row.findViewById(R.id.notify_detail_text);
        tvText.setText(notify_log.get(position).getText());
        return row;
    }
}
