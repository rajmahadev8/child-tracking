package com.infinity.childtracking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infinity.childtracking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallLogAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<HashMap<String,String>> callData;


    public CallLogAdapter(Context context, ArrayList<HashMap<String, String>> callData) {
        this.context = context;
        this.callData = callData;
    }

    @Override
    public int getCount() {
        return callData.size();
    }

    @Override
    public Object getItem(int position) {
        return callData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.call_list_model,parent,false);

        TextView tvNumber,tvName;
        tvNumber = row.findViewById(R.id.call_log_number);
        tvName = row.findViewById(R.id.call_log_name);

        HashMap<String,String> map = callData.get(position);
        for(Map.Entry<String,String> entry :  map.entrySet()){
            tvNumber.setText(entry.getKey());
            tvName.setText(entry.getValue());
           break;
        }
        return row;
    }

}
