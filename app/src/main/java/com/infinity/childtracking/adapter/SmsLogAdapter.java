package com.infinity.childtracking.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.infinity.childtracking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsLogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> smsData;

    public SmsLogAdapter(Context context, ArrayList<HashMap<String, String>> smsData) {
        this.context = context;
        this.smsData = smsData;
    }

    @Override
    public int getCount() {
        return smsData.size();
    }

    @Override
    public Object getItem(int position) {
        return smsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.sms_list_model,parent,false);

        TextView tvNumber,tvName;
        tvNumber = row.findViewById(R.id.sms_log_address);
        tvName = row.findViewById(R.id.sms_log_body);

        HashMap<String,String> map = smsData.get(position);
        for(Map.Entry<String,String> entry :  map.entrySet()){
            tvNumber.setText(entry.getKey());
            tvName.setText(entry.getValue());
            break;
        }
        return row;
    }
}
