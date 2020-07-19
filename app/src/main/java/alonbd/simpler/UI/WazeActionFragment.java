package alonbd.simpler.UI;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.WazeAction;

public class WazeActionFragment extends ActionFragment {

    private ChooseFromMapFragment mChoosePositionFragment;

    private static final String TAG = "ThugWazeActionFragment";

    private String mTaskName;

    @Override
    public Action genAction() {
        int notifId = TasksManager.NotificationIdGenerator.getNewId(getContext());
        LatLng latLng = mChoosePositionFragment.getSelectedPostition();
        if(latLng == null) return null;
        Geocoder geocoder = new Geocoder(getContext());
        String addressName = latLng.latitude + ", " + latLng.longitude;
        try {
            List<Address> results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(results != null && results.size() > 0) {
                addressName = results.get(0).getAddressLine(0);
            }
        } catch(IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }


        return new WazeAction(notifId, mTaskName, addressName, latLng.latitude, latLng.longitude);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_action_waze, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        TaskBuilder builder = (TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();
        mChoosePositionFragment = (ChooseFromMapFragment) getChildFragmentManager().findFragmentById(R.id.choose_destination_fragment);
    }

}
