package alonbd.simpler.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import alonbd.simpler.R;

public class LocationTriggerFragment extends Fragment {
    private static final int SERVICES_ERROR_DIALOG_REQ = 9001;
    private static final int LOCATION_PERMISSION_REQ_CODE = 1234;
    private static final String TAG = "LocationTriggerFragment";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private boolean locationPermissionGranted;
    private GoogleMap map;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trigger_location, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isServicesOK()) {
            getLocationPermission();
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: Checking if Google Play Services are up to date.");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(available == ConnectionResult.SUCCESS) {
            //A ok
            Log.d(TAG, "isServicesOK: Google Play Services are ok.");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Resolvable
            Log.d(TAG, "isServicesOK: fixable error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, SERVICES_ERROR_DIALOG_REQ);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Can't activate Google map", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQ_CODE);
        }

    }

    private void initMap(){
        Log.d(TAG,"Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            Log.d(TAG,"Map initialized");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQ_CODE: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted =true;
                }else{
                    locationPermissionGranted = false;
                }
            }
            initMap();
        }
    }
}
