package alonbd.simpler.BackgroundAndroid;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.Task;

public class LocationService extends Service {
    private static final String TAG = "ThugLocationService";

    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private ArrayList<Task> mLocationTasks;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();
                Log.d(TAG, "onLocationResult: location: " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());

                for(Task t : mLocationTasks) {
                    if(t.triggerMatchLocation(mLastLocation)) {
                        t.start(LocationService.this);
                    }
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback, Looper.getMainLooper()
        );
    }

    private void loadLocationTasks() {
        ArrayList<Task> tmp = TasksManager.getInstance(this).getData();
        mLocationTasks = new ArrayList<>();
        for(Task t : tmp) {
            if(t.getTriggerClass() == LocationTrigger.class) {
                mLocationTasks.add(t);
            }
        }
        if(mLocationTasks.size() == 0) {
            Log.d(TAG, "loadLocationTasks: SERVICE STOP SELF");
            stopSelf();}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        loadLocationTasks();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
