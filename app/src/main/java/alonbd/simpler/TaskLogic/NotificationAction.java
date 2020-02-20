package alonbd.simpler.TaskLogic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import java.io.Serializable;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;

public class NotificationAction implements Action, Serializable {
    private final static String CHANNEL_ID = "NotificationActionChannel";
    private final static CharSequence CHANNEL_NAME = "SimplerTasks";
    final static int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    private int notificationId;
    private String mContent;
    private String mTaskName;
    private NotificationManager mManager;

    public NotificationAction(int notificationId,String mContent, String mTaskName) {
        this.notificationId = notificationId;
        this.mContent = mContent;
        this.mTaskName = mTaskName;
    }

    @Override
    public void onExecute(Context context) {
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(notificationId, generateNotification(context));
    }
    private Notification generateNotification(Context context){

        NotificationChannel channel;
        Notification.Builder builder = new Notification.Builder(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = mManager.getNotificationChannel(CHANNEL_ID);
            if(channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
                channel.enableLights(true);
                channel.setLightColor(Color.MAGENTA);
                channel.enableVibration(true);
                mManager.createNotificationChannel(channel);
            }
            builder.setChannelId(CHANNEL_ID);
            builder.setSubText("Notification Task");
        }
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setContentTitle(mContent);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText("The task '"+mTaskName+"' just got triggered.");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(context.getResources().getColor(R.color.primaryLightColor));
        }
        return builder.build();
    }
}
