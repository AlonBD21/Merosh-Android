package alonbd.simpler.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.Trigger;

public class LocationTriggerFragment extends TriggerFragment {
    private static final int SERVICES_ERROR_DIALOG_REQ = 9001;
    private static final int LOCATION_PERMISSION_REQ_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "ThugLocationFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private Spinner mRadiusSpinner;
    private ChooseFromMapFragment mChoosePositionFragment;
    private Button mPermissionButton;
    private RelativeLayout mNoPermissionLayout;
    private LinearLayout mWithPermissionLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trigger_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoPermissionLayout = view.findViewById(R.id.without_permission_layout);
        mWithPermissionLayout = view.findViewById(R.id.with_permission_layout);
        mRadiusSpinner = view.findViewById(R.id.radius_spinner);
        mChoosePositionFragment = (ChooseFromMapFragment) getChildFragmentManager().findFragmentById(R.id.choose_from_map_fragment);
        mPermissionButton = view.findViewById(R.id.permission_ask_button);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_radius_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRadiusSpinner.setAdapter(adapter);
        mRadiusSpinner.setSelection(0);

        mPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {FINE_LOCATION};
                requestPermissions(permissions, LOCATION_PERMISSION_REQ_CODE);
            }
        });

        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mNoPermissionLayout.setVisibility(View.GONE);
        } else {
            mWithPermissionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNoPermissionLayout.setVisibility(View.GONE);
                mWithPermissionLayout.setVisibility(View.VISIBLE);
                mChoosePositionFragment.notifyPermissionsUpdate();
            } else {
                Toast.makeText(getContext(), getContext().getString(R.string.permission_ask_failed_toast), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public Trigger genTrigger(boolean singleUse) {
        LatLng latLng = mChoosePositionFragment.getSelectedPostition();
        if(latLng == null) {
            Toast.makeText(getContext(), getString(R.string.trigger_no_location_err), Toast.LENGTH_SHORT).show();
            return null;
        }
        return new LocationTrigger(singleUse, latLng, Integer.parseInt(((TextView) mRadiusSpinner.getSelectedView()).getText().toString()));
    }
}
