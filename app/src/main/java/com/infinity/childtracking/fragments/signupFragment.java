package com.infinity.childtracking.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.User;
import com.infinity.childtracking.views.Child;
import com.infinity.childtracking.views.OtpVerify;
import com.infinity.childtracking.views.Parent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class signupFragment extends Fragment {
    String title;
    int pgNO;

    EditText username,passowrd,cnfpassword,email_phone,child_user;
    RadioGroup userType;
    Button signupBtn;
    RadioButton rb;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Retrofit retrofit;
    RetroAPI retroAPI;

    String usertype;

    public signupFragment(int contentLayoutId, String title, int pgNO) {
        super(contentLayoutId);
        this.title = title;
        this.pgNO = pgNO;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = getActivity().findViewById(R.id.s_username);
        passowrd = getActivity().findViewById(R.id.s_password);
        cnfpassword = getActivity().findViewById(R.id.s_cnfpassword);
        email_phone = getActivity().findViewById(R.id.s_email_number);
        userType = getActivity().findViewById(R.id.userType);
        signupBtn = getActivity().findViewById(R.id.signupBtn);

        pref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        //if parent then show enter child username
        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int userId = userType.getCheckedRadioButtonId();
                rb = getActivity().findViewById(userId);

                usertype = rb.getText().toString().trim();
                usertype = (usertype.equalsIgnoreCase("I'm Child"))?"Child":"Parent";
                child_user = getActivity().findViewById(R.id.s_childUser);

                if(usertype.equals("Parent")){
                    child_user.setVisibility(View.VISIBLE);
                }else{
                    child_user.setVisibility(View.GONE);
                }
            }
        });

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        //on signup if cnf psw is equal to psw
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnfPsw = cnfpassword.getText().toString().trim();
                if(cnfPsw.equals(passowrd.getText().toString().trim())){
                    if(DataBank.notAllowUsernames.contains(username.getText().toString().trim())){
                        Toast.makeText(getContext(),"Username is already taken",Toast.LENGTH_LONG)
                                .show();
                    }else {
                        SignUp();
                    }
                }else{
                    Toast.makeText(getContext(),"Passwords not matched",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        checkLogin();
    }

    private void SignUp(){
        String uname = username.getText().toString().trim();
        String psw = passowrd.getText().toString().trim();
        String mail_mobile = email_phone.getText().toString().trim();
        String child_parent = (child_user != null)?child_user.getText().toString().trim():"null";
        Log.d("Signup",child_parent);
        DataBank.curUser = new User(uname,psw,mail_mobile, usertype,child_parent);
        startActivity(new Intent(getActivity(), OtpVerify.class));
        getActivity().finish();
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

    private boolean isLoggedin(){

        if(pref == null)
            pref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        return pref.contains("Username");
    }

}
