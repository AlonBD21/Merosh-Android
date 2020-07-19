package alonbd.simpler.TaskLogic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import alonbd.simpler.R;

public class NotificationAction implements Action, Serializable {
    public final static String CHANNEL_ID = "NotificationActionChannel";
    private final static CharSequence CHANNEL_NAME = "SimplerTasks";

    private int mNotificationId;
    private String mContent;
    private String mTaskName;
    private int mColor;

    public NotificationAction(int mNotificationId, String mContent, String mTaskName, int mColor) {
        this.mNotificationId = mNotificationId;
        this.mContent = mContent;
        this.mTaskName = mTaskName;
        this.mColor = mColor;
    }

    @Override
    public void onExecute(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(mNotificationId, generateNotification(context));
    }

    private Notification generateNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notification_bell);
        builder.setContentTitle(mContent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setChannel(context);
            builder.setChannelId(CHANNEL_ID);
        }
        builder.setContentText("The task '" + mTaskName + "' just got triggered.");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(mColor);
        }
        return builder.build();
    }

    public static void setChannel(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
                channel.setLightColor(Color.MAGENTA);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public View getDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_notif, null);
        ((TextView) view.findViewById(R.id.content_tv)).setText(mContent);
        view.findViewById(R.id.color_view).setBackgroundColor(mColor);
        return view;
    }
}
