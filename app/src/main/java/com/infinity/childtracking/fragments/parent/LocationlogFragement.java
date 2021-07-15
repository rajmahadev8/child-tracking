package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Bundle;
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
import com.infinity.childtracking.adapter.LocationLogAdapter;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.util.asyncTask;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationlogFragement extends Fragment implements View.OnKeyListener {

    private String title;
    int pageNo;

    ListView locLogview;
    ProgressBar bar;
    TextView tvToolbar;

    Retrofit retrofit;
    RetroAPI retroAPI;

    ArrayList<String> locLog;

    public LocationlogFragement(int contentLayoutId, String title, int pageNo) {
        super(contentLayoutId);
        this.title = title;
        this.pageNo = pageNo;
    }

    public LocationlogFragement() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locLog = new ArrayList<String>();
        try {
            tvToolbar = getActivity().findViewById(R.id.tvToolbar);
            tvToolbar.setText("Location Logs");
        } catch (Exception e) {
            e.printStackTrace();
        }

        bar = view.findViewById(R.id.notify_log_process);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        getLocationLog(new asyncTask() {
            @Override
            public void actionPerformed() {
                for (int i = 0; i < DataBank.location_log.size(); i++) {
                    locLog.add(DataBank.location_log.get(i).getLatitude());
                }

                HashSet<String> hashSet = new HashSet<>(locLog);
                locLog.clear();
                locLog.addAll(hashSet);

                locLogview = view.findViewById(R.id.notify_listview);
                LocationLogAdapter adapter = new LocationLogAdapter(getContext(), R.layout.location_list_model, locLog);
                locLogview.setAdapter(adapter);

                locLogview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), LocationFragmentActivity.class);
                        String str = ((TextView) view.findViewById(R.id.loc_log_title)).getText().toString();
                        intent.putExtra("selected", str);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void getLocationLog(asyncTask Task) {

        if (DataBank.location_log.isEmpty()) {

            String childUser = DataBank.curUser.getChild_parent();

            Call<ChildLocation> res = retroAPI.getLocation(childUser);
            res.enqueue(new Callback<ChildLocation>() {
                @Override
                public void onResponse(Call<ChildLocation> call, Response<ChildLocation> response) {
                    DataBank.location_log.add(response.body());
                    bar.setVisibility(View.GONE);
                    Task.actionPerformed();
                }

                @Override
                public void onFailure(Call<ChildLocation> call, Throwable t) {

                }
            });
        } else {
            bar.setVisibility(View.GONE);
            Task.actionPerformed();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragement()).commit();
        }
        return false;
    }
}