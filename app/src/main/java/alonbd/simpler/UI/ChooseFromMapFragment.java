package alonbd.simpler.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.R;

public class ChooseFromMapFragment extends Fragment {
    private static final int SERVICES_ERROR_DIALOG_REQ = 9001;
    private static final int LOCATION_PERMISSION_REQ_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "ThugLocationFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private EditText mSearchEt;
    private MarkerOptions mMarkerOptions;
    private Marker mMarker;
    private List<Address> mAddressList;
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_from_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchEt = view.findViewById(R.id.search);
        mSearchEt.setEnabled(false);

        GoogleApiAvailability availabilityHelper = GoogleApiAvailability.getInstance();
        int availabilityCode = availabilityHelper.isGooglePlayServicesAvailable(getContext());
        if(availabilityCode == ConnectionResult.SUCCESS) {
            initMap();
        } else if(availabilityHelper.isUserResolvableError(availabilityCode)) {
            Log.d(TAG, "isServicesOK: Fixable error");
            Dialog dialog = availabilityHelper.getErrorDialog(getActivity(), availabilityCode, SERVICES_ERROR_DIALOG_REQ);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "isServiceOK: Can't activate Google Play services", Toast.LENGTH_SHORT).show();
            //TODO map not available
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(false);
            if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
                selfLocateFocusAndMark();
            }
            mSearchEt.setEnabled(true);
            mGoogleMap.setOnMapClickListener(latLng -> {
                setMarker(latLng);
            });
            mSearchEt.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == KeyEvent.ACTION_DOWN
                        || actionId == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
                    mSearchEt.clearFocus();
                    geoLocateFocusAndMark(mSearchEt.getText().toString());
                }
                return true;
            });
        });

    }

    private void setMarker(LatLng latLng) {
        if(mMarkerOptions == null || mMarker == null) {
            mMarkerOptions = new MarkerOptions();
            mMarkerOptions.draggable(true);
            mMarkerOptions.position(latLng);
            mMarker = mGoogleMap.addMarker(mMarkerOptions);
        } else {
            mMarker.setPosition(latLng);
        }

    }

    private void geoLocateFocusAndMark(String toLookFor) {
        Geocoder geocoder = new Geocoder(getContext());
        mAddressList = new ArrayList<>();
        new Thread(() -> {
            try {
                mAddressList = geocoder.getFromLocationName(toLookFor, 1);
            } catch(IOException e) {
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
            }
            mHandler.post(() -> {
                if(mAddressList.size() > 0) {
                    Address address = mAddressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                    setMarker(latLng);
                }
            });
        }).start();

    }

    private void selfLocateFocusAndMark() {
        mLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        Task location = mLocationClient.getLastLocation();
        location.addOnCompleteListener(task -> {
            Location currentLocation = (Location) task.getResult();
            if(task.isSuccessful() && currentLocation != null) {
                Log.d(TAG, "selfLocate: onComplete: location found");
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                setMarker(latLng);
            }
        });
    }

    public void notifyPermissionsUpdate() {
        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            selfLocateFocusAndMark();
        }
    }

    public LatLng getSelectedPostition() {
        if(mMarker == null) return null;
        else return mMarker.getPosition();
    }


}
