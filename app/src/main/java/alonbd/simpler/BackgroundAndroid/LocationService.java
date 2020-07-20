package alonbd.simpler.BackgroundAndroid;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.R;

public class LocationService extends Service {
    private static final String TAG = "ThugLocationService";
    private static final String BOOLEAN_STOP_SERVICE_EXTRA = "actionStopService";

    private final static String CHANNEL_ID = "LocationServiceNotificationChannel";
    private final static CharSequence CHANNEL_NAME = "SimplerService";
    private final static int NOTIF_ID = 111;
    private final static int REQ_CODE = -50;

    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

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
                mLastLocation = locationResult.getLastLocation();
                Log.d(TAG, "onLocationResult: location: " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                if(TasksManager.startWithCondition(LocationService.this, mLastLocation)) {
                    Notification notification = generateNotification();
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIF_ID, notification);
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback, Looper.getMainLooper()
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        startForeground(NOTIF_ID, generateNotification());
        if(intent != null && intent.getBooleanExtra(BOOLEAN_STOP_SERVICE_EXTRA, false)) {
            stopSelf();
        }
        return Service.START_STICKY;
    }

    public Notification generateNotification() {
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
                manager.createNotificationChannel(channel);
            }
        }

        String notificationContent = TasksManager.getInstance(this).getLocationTasksString();
        if(notificationContent == null) {
            notificationContent = getString(R.string.service_no_more_tasks);
            stopSelf();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setChannelId(CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification_service);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getResources().getColor(R.color.primaryLightColor));
        }
        builder.setSubText(getString(R.string.service_notif_sub));
        builder.setContentTitle(getString(R.string.service_notif_title)).setContentText(getString(R.string.service_notif_content, notificationContent));
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            builder.setContentTitle(getString(R.string.service_notif_need_permission_title));
            builder.setContentText(getString(R.string.service_notif_need_presmission_text));
        }
        //Actions
        Intent stopIntent = new Intent(this, LocationService.class);
        stopIntent.putExtra(BOOLEAN_STOP_SERVICE_EXTRA,true);
        PendingIntent stopPending = PendingIntent.getService(this, REQ_CODE, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action notifAction = new NotificationCompat.Action(android.R.drawable.ic_delete, getString(R.string.service_stop), stopPending);


        builder.addAction(notifAction);
        return builder.build();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIF_ID);
    }

    public static String geoCode(Context context, double lat, double lng) {
        String location = "" + lat + "," + lng;
        Geocoder geocoder = new Geocoder(context);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if(list.size() > 0) {
            Address address = list.get(0);
            location = address.getAddressLine(0);
        }
        return location;
    }
}
