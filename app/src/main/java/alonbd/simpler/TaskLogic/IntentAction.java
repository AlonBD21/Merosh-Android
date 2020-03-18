package alonbd.simpler.TaskLogic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import java.io.Serializable;

import alonbd.simpler.R;

public abstract class IntentAction implements Action, Serializable {
    private int mNotificationId;
    private String mTaskName;

    @Override
    public void onExecute(Context context) {
        NotificationAction.setChannel(context);
        Notification.Builder builder = new Notification.Builder(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NotificationAction.CHANNEL_ID);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getNotificationColorInt());
        }
        builder.setSubText("Notification Task");
        builder.setSmallIcon(getNotificationIconID());
        builder.setContentTitle(getNotificationTitleString());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(getNotificationContentString());
        builder.setContentIntent(getPendingIntent(context));
        builder.setSubText(getSubtext());
        NotificationManager manager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));

        manager.notify(mNotificationId,builder.build());

    }

    public IntentAction(int notificationId,String mTaskName) {
        this.mNotificationId = notificationId;
        this.mTaskName = mTaskName;
    }

    public int getNotificationId() {
        return mNotificationId;
    }
    public String getSubtext(){
        return "Task '"+mTaskName+"'";
    }
    public abstract String getNotificationContentString();
    public abstract String getNotificationTitleString();
    public abstract int getNotificationIconID();
    public abstract int getNotificationColorInt( );
    public abstract PendingIntent getPendingIntent(Context context);
}
