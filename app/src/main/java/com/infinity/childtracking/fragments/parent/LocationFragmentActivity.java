package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infinity.childtracking.R;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.util.asyncTask;
import com.infinity.childtracking.views.Parent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationFragmentActivity extends FragmentActivity implements OnMapReadyCallback {

    Double latitude = -34.0, longitude = 151.0;
    GoogleMap map;
    TextView tvToolbar;

    public LocationFragmentActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_location_fragment);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Current Location");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getChildLocation(new asyncTask() {
            @Override
            public void actionPerformed() {
                onMapReady(map);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Parent.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void getChildLocation(asyncTask task) {
        Retrofit retrofit = RetroClient.getRetrofit();
        RetroAPI retroAPI = retrofit.create(RetroAPI.class);

        Call<ChildLocation> res = retroAPI.getLocation(DataBank.curUser.getChild_parent());
        res.enqueue(new Callback<ChildLocation>() {
            @Override
            public void onResponse(Call<ChildLocation> call, Response<ChildLocation> response) {
                if (response.code() == 200) {
                    Log.d("CHILDLOCA", response.body().toString());
                    latitude = Double.valueOf(response.body().getLatitude());
                    longitude = Double.valueOf(response.body().getLongitude());
                    task.actionPerformed();
                }
            }

            @Override
            public void onFailure(Call<ChildLocation> call, Throwable t) {

            }
        });
    }
}