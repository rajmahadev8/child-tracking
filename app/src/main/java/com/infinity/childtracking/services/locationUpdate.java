package com.infinity.childtracking.services;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;

import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class locationUpdate implements LocationListener {
    Double latitude,longitude;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        insertLocation();
    }


    private void insertLocation(){
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        ChildLocation location = new ChildLocation(
                String.valueOf(latitude),
                String.valueOf(longitude),
                DataBank.curUser.getUsername()
        );

        Call<APIResponse> res = retroAPI.insertLocation(location);
        res.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("Loc","success");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("Loc","failure");
            }
        });
    }
}
