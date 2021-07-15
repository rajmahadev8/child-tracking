package com.infinity.childtracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.infinity.childtracking.R;
import com.infinity.childtracking.data.DataBank;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsageLogAdapter extends ArrayAdapter<String> {

    private Context context;

    private ArrayList<String> pkg_list = new ArrayList<>();

    public UsageLogAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.pkg_list = objects;
    }


    @Override
    public int getCount() {
        return pkg_list.size();
    }

    @Override
    public String getItem(int position) {
        return pkg_list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.usage_list_model,parent,false);

        TextView tvPkg;
        tvPkg = row.findViewById(R.id.use_log_pkg);
        String tmp = pkg_list.get(position);
        tmp = tmp.substring(tmp.lastIndexOf(".")+1,tmp.length());
        tvPkg.setText(tmp);
        return row;
    }

}
