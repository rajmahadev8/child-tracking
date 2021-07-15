package com.infinity.childtracking.fragments.child;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.services.ChildNotify;
import com.infinity.childtracking.services.CstmListner;
import com.infinity.childtracking.views.ChildHideIcon;

public class SetupNotification extends Fragment implements CstmListner {
    Button setBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_notification,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBtn = view.findViewById(R.id.set_notification);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getContext(), ChildNotify.class));
                new ChildNotify().setListner(SetupNotification.this::setValue);
                requestNotifyAccess();
            }
        });
    }

    @Override
    public void setValue(String pkg) {
        Log.d("Notify","In Child "+pkg);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void requestNotifyAccess() {
        Toast.makeText(getContext(), "Give Notification Access", Toast.LENGTH_LONG)
                .show();
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }
}
