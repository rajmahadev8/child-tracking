package com.infinity.childtracking.fragments.child;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.infinity.childtracking.R;
import com.infinity.childtracking.api.APIResponse;
import com.infinity.childtracking.api.RetroAPI;
import com.infinity.childtracking.api.RetroClient;
import com.infinity.childtracking.data.DataBank;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.services.locationUpdate;
import com.infinity.childtracking.util.asyncTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.LOCATION_SERVICE;

public class SetupLocation extends Fragment {
    Button locationBtn;

    Retrofit retrofit;
    RetroAPI retroAPI;

    Double latitude, longitude;

    FusedLocationProviderClient mFusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_location, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFusedLocationClient = new FusedLocationProviderClient(getActivity());

        retrofit = RetroClient.getRetrofit();
        retroAPI = retrofit.create(RetroAPI.class);

        locationBtn = view.findViewById(R.id.set_location);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getData() {
        if (HavePermission()) {
            getLastLocation(new asyncTask() {
                @Override
                public void actionPerformed() {
                    insertLocation();
                }
            });
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 1024);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean HavePermission() {
        return getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void getLastLocation(asyncTask task) {
        if (isLocationEnabled()) {

            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        if (latitude != null && longitude != null) {
            task.actionPerformed();
            Log.d("Loc", "have location");
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5)
                .setFastestInterval(0)
                .setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1024) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(new asyncTask() {
                    @Override
                    public void actionPerformed() {
                        insertLocation();
                    }
                });
            }
        }
    }

    private void insertLocation() {
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
                Log.d("Loc", "success");
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d("Loc", "failure");
            }
        });
    }

}
