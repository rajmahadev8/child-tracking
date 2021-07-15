package com.infinity.childtracking.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.infinity.childtracking.R;

import java.util.ArrayList;

public class NotifyLogAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> titles;

    public NotifyLogAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.titles = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.notify_list_model,null,true);

        TextView textView = row.findViewById(R.id.notify_log_title);
        textView.setText(titles.get(position));
        return row;
    }
}
