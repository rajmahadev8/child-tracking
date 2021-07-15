package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.CallLogAdapter;
import com.infinity.childtracking.adapter.UsageLogAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.CallDetail;
import com.infinity.childtracking.views.Parent;
import com.infinity.childtracking.views.UsageDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsageFragement extends Fragment implements View.OnKeyListener {

    ListView usageLogview;
    ProgressBar bar;
    EditText etSearch;
    TextView tvToolbar;

    Retrofit retrofit;
    RetroAPI retroAPI;

    ArrayList<String> pkg_list = new ArrayList<>();

    public UsageFragement() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = RetroClient.getRetrofit();
        retroAPI= retrofit.create(RetroAPI.class);
        tvToolbar = getActivity().findViewById(R.id.tvToolbar);
        tvToolbar.setText("App Usages");

        bar = view.findViewById(R.id.usage_log_process);
        etSearch = view.findViewById(R.id.usage_search);

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

        usageLogview = view.findViewById(R.id.usage_listview);

        getUsage(new asyncTask() {
            @Override
            public void actionPerformed() {
                for(int i =0;i< DataBank.usages_log.size();i++){
                    String pkg = DataBank.usages_log.get(i).getPkg();
                    if(i>=1 && !pkg_list.contains(pkg)){
                        pkg_list.add(pkg);
                    }
                }
                Log.d("HII",pkg_list.size()+ "");
                UsageLogAdapter adapter = new UsageLogAdapter(getContext(),R.layout.usage_list_model
                        ,pkg_list);
                usageLogview.setAdapter(adapter);

                usageLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), UsageDetail.class);
                        String str = ((TextView) view.findViewById(R.id.use_log_pkg)).getText().toString();
                        intent.putExtra("selected",str);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parent_usage_fragment,container,false);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            startActivity(new Intent(getContext(), Parent.class));
            getActivity().finish();
        }
        return false;
    }

    private void getUsage(asyncTask task) {
        if (DataBank.usages_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Call<ArrayList<USAGE>> res = retroAPI.getUsage(childUser);
            res.enqueue(new Callback<ArrayList<USAGE>>() {
                @Override
                public void onResponse(Call<ArrayList<USAGE>> call, Response<ArrayList<USAGE>> response) {
                    if(response.code() == 200){
                        DataBank.usages_log = response.body();
                        bar.setVisibility(View.GONE);
                        task.actionPerformed();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<USAGE>> call, Throwable t) {

                }
            });
        }
        else {
            bar.setVisibility(View.GONE);
            task.actionPerformed();
        }
    }

    private void updateLV(String s) {
        ArrayList<String> tmp = new ArrayList<>();
        if(s.isEmpty())
            tmp = pkg_list;
        for(int i=0;i<pkg_list.size();i++){
            if(pkg_list.get(i).contains(s)){
                tmp.add(pkg_list.get(i));
            }
        }
        UsageLogAdapter adapter = new UsageLogAdapter(getContext(),R.layout.usage_list_model
                ,tmp);
        usageLogview.setAdapter(adapter);

        usageLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), UsageDetail.class);
                String str = ((TextView) view.findViewById(R.id.use_log_pkg)).getText().toString();
                intent.putExtra("selected",str);
                startActivity(intent);
            }
        });

    }

}