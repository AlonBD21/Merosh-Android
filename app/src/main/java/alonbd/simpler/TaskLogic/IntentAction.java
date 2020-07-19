package alonbd.simpler.TaskLogic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.io.Serializable;

import alonbd.simpler.R;

public abstract class IntentAction implements Action, Serializable {
    private int mNotificationId;

    @Override
    public void onExecute(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationAction.setChannel(context);
        }
        Notification.Builder builder = new Notification.Builder(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NotificationAction.CHANNEL_ID);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getNotificationColorInt());
        }
        builder.setSubText(context.getString(R.string.notif_reminder_activated));
        builder.setSmallIcon(getNotificationIconID());
        builder.setContentTitle(getNotificationTitleString(context));
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(getNotificationContentString(context));
        builder.setContentIntent(getPendingIntent(context));
        NotificationManager manager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));

        manager.notify(mNotificationId,builder.build());

    }

    public IntentAction(int notificationId,String mTaskName) {
        this.mNotificationId = notificationId;
    }

    public int getNotificationId() {
        return mNotificationId;
    }

    public abstract String getNotificationContentString(Context context);

    public abstract String getNotificationTitleString(Context context);
    public abstract int getNotificationIconID();
    public abstract int getNotificationColorInt( );
    public abstract PendingIntent getPendingIntent(Context context);
}
