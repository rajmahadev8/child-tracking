package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.adapter.NotifyLogAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.NotifyDetail;
import com.infinity.childtracking.views.Parent;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotifylogFragement extends Fragment implements View.OnKeyListener {

    private String title;
    int pageNo;

    ListView notifyLogview;
    ProgressBar bar;
    TextView tvToolbar;

    Retrofit retrofit;
    RetroAPI retroAPI;

    ArrayList<String> notifyLog;

    public NotifylogFragement(int contentLayoutId, String title, int pageNo) {
        super(contentLayoutId);
        this.title = title;
        this.pageNo = pageNo;
    }

    public NotifylogFragement() {  }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notifyLog = new ArrayList<String>();
        try {
            tvToolbar = getActivity().findViewById(R.id.tvToolbar);
            tvToolbar.setText("Notification Logs");
        }catch (Exception e){
            e.printStackTrace();
        }
        bar = view.findViewById(R.id.notify_log_process);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        getNotifyLog(new asyncTask() {
            @Override
            public void actionPerformed() {
                for (int i = 0; i < DataBank.notifications.size(); i++) {
                    notifyLog.add(DataBank.notifications.get(i).getPkg());
                }

                HashSet<String> hashSet = new HashSet<>(notifyLog);
                notifyLog.clear();
                notifyLog.addAll(hashSet);

                for(int i=0;i<notifyLog.size();i++)
                {
                    String tmp = notifyLog.get(i);
                    tmp = tmp.substring(tmp.lastIndexOf(".")+1,tmp.length());
                    notifyLog.set(i,tmp);
                }

                notifyLogview = view.findViewById(R.id.notify_listview);
                NotifyLogAdapter adapter = new NotifyLogAdapter(getContext(), R.layout.notify_list_model, notifyLog);
                notifyLogview.setAdapter(adapter);

                notifyLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), NotifyDetail.class);
                        String str = ((TextView) view.findViewById(R.id.notify_log_title)).getText().toString();
                        intent.putExtra("selected", str);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void getNotifyLog(asyncTask Task) {

        if (DataBank.notifications.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();
            Log.d("Notify",childUser);
            Call<ArrayList<ChildNotification>> res = retroAPI.getNotify(childUser);
            res.enqueue(new Callback<ArrayList<ChildNotification>>() {
                @Override
                public void onResponse(Call<ArrayList<ChildNotification>> call, Response<ArrayList<ChildNotification>> response) {
                    Log.d("Notify",String.valueOf(response.body().size()));
                    if (response.code() == 200) {
                        DataBank.notifications = response.body();
                        Log.d("Notify",response.body().toString());
                        bar.setVisibility(View.GONE);
                        Task.actionPerformed();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChildNotification>> call, Throwable t) {
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