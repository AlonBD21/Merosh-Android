package alonbd.simpler.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.R;

public class LocationTriggerFragment extends Fragment {
    private static final int SERVICES_ERROR_DIALOG_REQ = 9001;
    private static final int LOCATION_PERMISSION_REQ_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "ThugLocationFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private FusedLocationProviderClient mLocationClient;
    private EditText mSearchEt;
    private MarkerOptions mMarkerOptions;
    private Marker mMarker;
    private List<Address> mAddressList;
    private Handler mHandler = new Handler();

    public Marker getMarker(){return mMarker;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trigger_location, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchEt = view.findViewById(R.id.search);
        if(isServicesOK()) {
            getLocationPermission();
            initMap();
        }
        mSearchEt.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == KeyEvent.ACTION_DOWN
                    || actionId == KeyEvent.KEYCODE_ENTER) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(),0);
                mSearchEt.clearFocus();
                geoLocate();
            }
            return true;
        });
    }

    private void geoLocate() {
        String searchString = mSearchEt.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        mAddressList = new ArrayList<>();
        new Thread(() -> {
            try {
                mAddressList = geocoder.getFromLocationName(searchString, 1);
            } catch(IOException e) {
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
            }
            mHandler.post(() -> {
                if(mAddressList.size() > 0) {
                    Address address = mAddressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    focusMap(latLng, DEFAULT_ZOOM);
                    setMarker(latLng);
                }
            });
        }).run();

    }

    private boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(available == ConnectionResult.SUCCESS) {
            //A ok
            Log.d(TAG, "isServicesOK: Google Play Services are ok.");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Resolvable
            Log.d(TAG, "isServicesOK: Fixable error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, SERVICES_ERROR_DIALOG_REQ);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "isServiceOK: Can't activate Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQ_CODE);
        }

    }

    private void selfLocate() {
        mLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if(mLocationPermissionGranted) {
            Task location = mLocationClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                Location currentLocation = (Location) task.getResult();
                if(task.isSuccessful() && currentLocation != null) {
                    Log.d(TAG, "selfLocate: onComplete: location found");
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    focusMap(latLng, DEFAULT_ZOOM);
                    setMarker(latLng);
                } else {
                    Log.d(TAG, "selfLocate: onComplete: location is null");
                    Toast.makeText(getContext(), "Could not find current location", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Can not find current location without permission", Toast.LENGTH_SHORT).show();

        }
    }

    private void focusMap(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void setMarker(LatLng latLng) {
        if(mMarkerOptions == null || mMarker==null) {
            mMarkerOptions = new MarkerOptions();
            mMarkerOptions.draggable(true);
            mMarkerOptions.title("Task Activation Spot");
            mMarkerOptions.position(latLng);
            mMarker = mMap.addMarker(mMarkerOptions);
        } else {
            mMarker.setPosition(latLng);
        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            if(mLocationPermissionGranted) {
                selfLocate();
                mMap.setMyLocationEnabled(true);
            }
            mMap.setOnMapClickListener(latLng -> {
                setMarker(latLng);
            });
    });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQ_CODE: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    mLocationPermissionGranted = false;
                }
            }
            initMap();
        }
    }
}
