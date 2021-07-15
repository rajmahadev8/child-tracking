package com.infinity.childtracking.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.Token;
import com.infinity.childtracking.model.User;
import com.infinity.childtracking.views.Child;
import com.infinity.childtracking.views.Parent;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class loginFragment extends Fragment {
    String title;
    int pgNO;

    EditText etUname,etPsw;
    Button loginBtn,forgotBtn;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Retrofit retrofit;
    RetroAPI retroAPI;

    public loginFragment(int contentLayoutId, String title, int pgNO) {
        super(contentLayoutId);
        this.title = title;
        this.pgNO = pgNO;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUname = view.findViewById(R.id.etUname);
        etPsw = view.findViewById(R.id.etPsw);

        forgotBtn = view.findViewById(R.id.forgotBtn);
        loginBtn = view.findViewById(R.id.loginBtn);

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        getUsernameList();

        pref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
        checkLogin();
    }

    private void doLogin(){
        if(!isLoggedin()){
            String uname = etUname.getText().toString().trim();
            String psw = etPsw.getText().toString().trim();

            Call<User> user =  retroAPI.login(new User(uname,psw));
            user.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200){
                        if(pref == null)
                            pref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                        editor = pref.edit();
                        editor.putString("Username",uname);
                        editor.putString("UserType",response.body().getUsertype());
                        editor.putString("childUser",response.body().getChild_parent());
                        editor.commit();
                        manageToken(response.body().getUsertype());
                        DataBank.curUser = new User(uname,psw,response.body().getEmail_mobile(),
                                response.body().getUsertype(),response.body().getChild_parent());
                        Intent intent;
                        if(response.body().getUsertype().equalsIgnoreCase("child"))
                            intent = new Intent(getContext(), Child.class);
                        else if(response.body().getUsertype().equalsIgnoreCase("parent"))
                            intent = new Intent(getContext(), Parent.class);
                        else
                            intent = null;
                        if(intent !=null) {
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("Login","Failed "+t.getMessage());
                }
            });
        }
    }

    private boolean isLoggedin(){

        if(pref == null)
            pref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        return pref.contains("Username");
    }

    private void checkLogin(){
        if(isLoggedin()){
            Intent intent;
            if(pref.getString("UserType","").equalsIgnoreCase("child"))
                intent = new Intent(getContext(),Child.class);
            else
                intent = new Intent(getContext(),Parent.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void getUsernameList() {
        if(!isLoggedin()){
            Call<ArrayList<String>> res = retroAPI.getUsers();
            res.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    DataBank.notAllowUsernames = response.body();
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                }
            });
        }
    }

    private void forgotPassword() {
        String uname = etUname.getText().toString().trim();
        String psw = etPsw.getText().toString().trim();
        if(uname.isEmpty() && psw.isEmpty()){
            Toast.makeText(getContext(),"Fill with new Password",Toast.LENGTH_LONG).show();
        }else{
            User newUser = new User(uname,psw);
            Call<User> res = retroAPI.forgotPass(newUser);
            res.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200){
                        Toast.makeText(getContext(),"Your password is changed,Log you in !",
                                Toast.LENGTH_LONG).show();
                        DataBank.curUser = new User(uname,response.body().getUsertype(),
                                response.body().getChild_parent());
                        doLogin();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private void manageToken(String utype){
        if(DataBank.curToken != null){
            if(utype.equalsIgnoreCase("parent")){
                Token token = new Token(DataBank.curToken.getToken()
                        ,DataBank.curUser.getUsername());
                DataBank.curToken = token;
                insertToken();
            }else{
                DataBank.curToken = null;
            }
        }
    }

    private void insertToken() {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        Call<APIResponse> res =  retroAPI.insertToken(DataBank.curToken);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("TOKEN","Success ");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("TOKEN","Success "+ t.getMessage());
            }
        });
    }
}
