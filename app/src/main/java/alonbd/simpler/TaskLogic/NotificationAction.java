package alonbd.simpler.TaskLogic;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import java.io.Serializable;

public class NotificationAction implements Action, Serializable {
    public final static String CHANNEL_ID = "NotificationActionChannel";
    public final static CharSequence CHANNEL_NAME = "Simpler";
    final static int IMPOERTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    String content;

    public NotificationAction(String content){
        this.content = content;

    }

    @Override
    public void onExecute(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null){
                channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,IMPOERTANCE);
                channel.enableLights(true);
                channel.setLightColor(Color.CYAN);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
            }
        }

    }
}
