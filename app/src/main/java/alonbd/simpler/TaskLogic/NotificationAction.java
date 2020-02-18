package alonbd.simpler.TaskLogic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import java.io.Serializable;

import alonbd.simpler.R;

public class NotificationAction implements Action, Serializable {
    private final static String CHANNEL_ID = "NotificationActionChannel";
    private final static CharSequence CHANNEL_NAME = "Simpler";
    private final static int ID = 5;//TODO Change to dynamic id
    final static int IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    private String mContent;
    private String mTaskName;

    public NotificationAction(String mContent, String mTaskName) {
        this.mContent = mContent;
        this.mTaskName = mTaskName;
    }

    @Override
    public void onExecute(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        Notification.Builder builder = new Notification.Builder(context);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
                channel.enableLights(true);
                channel.setLightColor(Color.CYAN);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
            }
            builder.setChannelId(CHANNEL_ID);
        }
        builder.setSmallIcon(R.drawable.ic_notification_bell);
        builder.setContentTitle(mTaskName + " got triggered");
        builder.setContentText(mContent);
        manager.notify(ID, builder.build());

    }
}
