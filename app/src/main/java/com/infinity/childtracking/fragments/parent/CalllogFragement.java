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
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallLogAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.CallDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalllogFragement extends Fragment {

    private String title;
    int pageNo;

    ListView callLogview;
    ProgressBar bar;
    EditText etSearch;
    TextView tvToolbar;

    Retrofit retrofit;
    RetroAPI retroAPI;

    ArrayList<HashMap<String,String>> callData;

    public CalllogFragement(int contentLayoutId, String title, int pageNo) {
        super(contentLayoutId);
        this.title = title;
        this.pageNo = pageNo;
    }

    public CalllogFragement() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callData = new ArrayList<>();
        bar = view.findViewById(R.id.call_log_process);
        etSearch = view.findViewById(R.id.call_search);
        try {
            tvToolbar = getActivity().findViewById(R.id.tvToolbar);
            tvToolbar.setText("Call Logs");
        }catch (Exception e){
            e.printStackTrace();
        }
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

        getCallLog(new asyncTask() {
            @Override
            public void actionPerformed() {

                for (int i = 0; i < DataBank.call_log.size(); i++) {
                    //callLog.add(DataBank.call_log.get(i).getNumber());
                    CALL call = DataBank.call_log.get(i);
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(call.getNumber(),call.getName());
                    if(callData!=null && !callData.contains(map))
                        callData.add(map);
                }

                callLogview = view.findViewById(R.id.call_listview);
                CallLogAdapter adapter = new CallLogAdapter(getContext(),callData);
                callLogview.setAdapter(adapter);

                callLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), CallDetail.class);
                        String str = ((TextView) view.findViewById(R.id.call_log_number)).getText().toString();
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
            tempData = callData;
        for(int i=0;i<callData.size();i++){
            HashMap<String,String> map = callData.get(i);
            for(Map.Entry<String,String> entry: map.entrySet()){
                if(entry.getValue().equalsIgnoreCase(s)){
                    tempData.add(map);
                }
                break;
            }
        }
        CallLogAdapter adapter = new CallLogAdapter(getContext(),tempData);
        callLogview.setAdapter(adapter);

        callLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CallDetail.class);
                String str = ((TextView) view.findViewById(R.id.call_log_number)).getText().toString();
                intent.putExtra("selected", str);
                startActivity(intent);
            }
        });
    }

    private void getCallLog(asyncTask Task) {

        if (DataBank.call_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();

            Call<ArrayList<CALL>> res = retroAPI.getCall(childUser);
            res.enqueue(new Callback<ArrayList<CALL>>() {
                @Override
                public void onResponse(Call<ArrayList<CALL>> call, Response<ArrayList<CALL>> response) {
                    if (response.code() == 200) {
                        DataBank.call_log = response.body();
                        bar.setVisibility(View.GONE);
                        Task.actionPerformed();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CALL>> call, Throwable t) {
                }
            });
        } else {
            bar.setVisibility(View.GONE);
            Task.actionPerformed();
        }
    }
}