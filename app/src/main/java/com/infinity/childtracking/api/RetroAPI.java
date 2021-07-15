package com.infinity.childtracking.api;

import androidx.core.content.pm.PermissionInfoCompat;

import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.model.SMS;
import com.infinity.childtracking.model.Token;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetroAPI {

    @POST("/login")
    Call<User> login(@Body User login);

    @POST("/signup")
    Call<APIResponse> signup(@Body User signup);

    @POST("/sendOtp")
    Call<String> sendOtp(@Body User signup);

    @POST("/getUsers")
    Call<ArrayList<String>> getUsers();

    @POST("/forgotPass")
    Call<User> forgotPass(@Body User newUser);

    @POST("/getChild")
    Call<ArrayList<String>> getChild(@Body String username);

    @POST("/updateUser")
    Call<APIResponse> updateUser(@Body User newUser);

    @POST("/insertCall")
    Call<APIResponse> insertCall(@Body ArrayList<CALL> call_log);

    @POST("/updateChild")
    Call<APIResponse> updateChild(@Body String newChild,@Body String username);

    @POST("/insertSMS")
    Call<APIResponse> insertSMS(@Body ArrayList<SMS> sms_log);

    @POST("/insertNotify")
    Call<APIResponse> insertNotify(@Body ChildNotification notification);

    @POST("/getCall")
    Call<ArrayList<CALL>> getCall(@Body String childUser);

    @POST("/getSms")
    Call<ArrayList<SMS>> getSms(@Body String childUser);

    @POST("/getNotify")
    Call<ArrayList<ChildNotification>> getNotify(@Body String childUser);

    @POST("/SendCall")
    Call<APIResponse> sendCall(@Body CALL call);

    @POST("/SendSms")
    Call<APIResponse> sendSms(@Body SMS sms);

    @POST("/insertToken")
    Call<APIResponse> insertToken(@Body Token token);

    @POST("/insertLocation")
    Call<APIResponse> insertLocation(@Body ChildLocation location);

    @POST("/getLocation")
    Call<ChildLocation> getLocation(@Body String username);

    @POST("/insertUsage")
    Call<APIResponse> insertUsage(@Body ArrayList<USAGE> usages);

    @POST("/getUsage")
    Call<ArrayList<USAGE>> getUsage(@Body String username);
}
