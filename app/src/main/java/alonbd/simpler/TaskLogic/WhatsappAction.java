package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import java.io.Serializable;

import alonbd.simpler.R;

public class WhatsappAction extends IntentAction implements Serializable {
    private String mContent;
    private String mTo;

    @Override
    public String getNotificationContentString() {
        return mContent;
    }

    @Override
    public String getNotificationTitleString() {
        return "Click to send message via WhatsApp";
    }

    @Override
    public int getNotificationIconID() { return R.drawable.ic_notification_whatsapp; }

    @Override
    public int getNotificationColorInt() {
        return Color.GREEN;
    }

    @Override
    public PendingIntent getPendingIntent(Context context)
    {
        String uri = "https://api.whatsapp.com/send?phone=$&text=!";
        uri = uri.replace("$",mTo);
        uri = uri.replace("!",mContent);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        return PendingIntent.getActivity(context,getNotificationId(),intent,0);
    }

    public WhatsappAction(int notificationId,String taskName,String mContent, String mTo) {
        super(notificationId,taskName);
        this.mContent = mContent;
        this.mTo = mTo;
    }
}
