package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallLogAdapter;
import com.infinity.childtracking.adapter.SmsLogAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.SMS;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.CallDetail;
import com.infinity.childtracking.views.Parent;
import com.infinity.childtracking.views.SMSDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MsglogFragment extends Fragment implements View.OnKeyListener {

    private String title;
    private int pageNo;

    ListView smsLogview;
    ProgressBar bar;
    EditText etSearch;
    TextView tvToolbar;

    Retrofit retrofit;
    RetroAPI retroAPI;

    ArrayList<HashMap<String,String>> smsData;

    public MsglogFragment(int contentLayoutId,String title,int pageNo) {
        super(contentLayoutId);
        this.title = title;
        this.pageNo = pageNo;
    }

    public MsglogFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            tvToolbar = getActivity().findViewById(R.id.tvToolbar);
            tvToolbar.setText("Sms Logs");
        }catch (Exception e){
            e.printStackTrace();
        }
        smsData = new ArrayList<>();
        bar = view.findViewById(R.id.sms_log_process);
        etSearch = view.findViewById(R.id.sms_search);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateLV(s.toString());
            }
        });

        getSmsLog(new asyncTask() {
            @Override
            public void actionPerformed() {
                for (int i = 0; i < DataBank.sms_log.size(); i++) {
                    SMS sms = DataBank.sms_log.get(i);
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(sms.getAddress(),sms.getBody());

                    if(smsData != null && !smsData.contains(map)){
                        smsData.add(map);
                    }
                }
                smsLogview = view.findViewById(R.id.sms_listview);
                SmsLogAdapter adapter = new SmsLogAdapter(getContext(), smsData);
                smsLogview.setAdapter(adapter);

                smsLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), SMSDetail.class);
                        String str = ((TextView) view.findViewById(R.id.sms_log_address)).getText().toString();
                        Log.d("HD",str);
                        intent.putExtra("selected", str);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void updateLV(String s) {
        ArrayList<HashMap<String,String>> tempData = new ArrayList<>();
        if(s.isEmpty())
            tempData = smsData;

        for(int i=0;i<smsData.size();i++){
            HashMap<String,String> map = smsData.get(i);
            for(Map.Entry<String,String> entry: map.entrySet()){
                if(entry.getValue().contains(s)){
                    tempData.add(map);
                }
                break;
            }
        }
        SmsLogAdapter adapter = new SmsLogAdapter(getContext(),tempData);
        smsLogview.setAdapter(adapter);

        smsLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CallDetail.class);
                String str = ((TextView) view.findViewById(R.id.sms_log_address)).getText().toString();
                intent.putExtra("selected", str);
                startActivity(intent);
            }
        });
    }

    private void getSmsLog(asyncTask Task) {
        if (DataBank.sms_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Call<ArrayList<SMS>> res = retroAPI.getSms(childUser);
            res.enqueue(new Callback<ArrayList<SMS>>() {
                @Override
                public void onResponse(Call<ArrayList<SMS>> call, Response<ArrayList<SMS>> response) {
                    if (response.code() == 200) {
                        DataBank.sms_log = response.body();
                        bar.setVisibility(View.GONE);
                        Task.actionPerformed();
                        Log.d("SMS",String.valueOf(DataBank.sms_log.size()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SMS>> call, Throwable t) {

                }
            });

        }else {
            bar.setVisibility(View.GONE);
            Task.actionPerformed();
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragement()).commit();
        }
        return false;
    }
}
