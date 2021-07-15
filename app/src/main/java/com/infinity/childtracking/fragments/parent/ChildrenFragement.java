package com.infinity.childtracking.fragments.parent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChildrenFragement extends Fragment {

    TextView tvToolbar;

    public void ChildrensFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parent_child_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button;
        tvToolbar = getActivity().findViewById(R.id.tvToolbar);
        tvToolbar.setText("Your Children");
        button = view.findViewById(R.id.addChild);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChild();
            }
        });

    }

    private void updateChild() {

        buildDialog();
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Child");
        String items[] = getChild();
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "You need to Re-Login", Toast.LENGTH_LONG)
                        .show();
                DataBank.curUser.setChild_parent(items[which]);
                updateUser();
                dialog.dismiss();
            }
        })
        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String[] getChild() {
        if (DataBank.child_users != null) {
            Retrofit retrofit = RetroClient.getRetrofit();
            RetroAPI retroAPI = retrofit.create(RetroAPI.class);

            Call<ArrayList<String>> res = retroAPI.getChild(DataBank.curUser.getUsername());
            res.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.code() == 200) {
                        DataBank.child_users = response.body();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                }
            });
        }
        return DataBank.child_users.toArray(new String[0]);
    }

    private void updateUser() {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        Call<APIResponse> res = retroAPI.updateUser(DataBank.curUser);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}