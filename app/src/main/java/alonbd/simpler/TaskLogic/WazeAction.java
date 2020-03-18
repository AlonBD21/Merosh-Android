package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import java.io.Serializable;

import alonbd.simpler.R;

public class WazeAction extends IntentAction implements Serializable {
    private double mLat;
    private double mLng;
    private String mLocationName;

    @Override
    public String getNotificationContentString() {
        return "Destination: "+ mLocationName;
    }

    @Override
    public String getNotificationTitleString() {
        return "Click to Navigate with Waze";
    }

    @Override
    public int getNotificationIconID() {
        return R.drawable.ic_notification_compass;
    }

    @Override
    public int getNotificationColorInt() {
        return Color.BLUE;
    }

    @Override
    public PendingIntent getPendingIntent(Context context) {
        String uri = "waze://?ll="+mLat+","+mLng+"&navigate=yes";
        String url = "https://www.waze.com/ul?ll="+mLat+","+mLng+"&navigate=yes";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        return PendingIntent.getActivity(context,getNotificationId(),intent,0);
    }

    public WazeAction(int notificationId, String mTaskName, String mLocationName, double mLat, double mLng) {
        super(notificationId, mTaskName);
        this.mLat = mLat;
        this.mLng = mLng;
        this.mLocationName = mLocationName;
    }
}
