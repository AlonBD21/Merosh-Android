package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import java.io.Serializable;

public class WhatsappAction extends IntentAction implements Serializable {
    private String mContent;

    @Override
    public String getNotificationContentString() {
        return mContent;
    }

    @Override
    public String getNotificationTitleString() {
        return "Click to send message via WhatsApp";
    }

    @Override
    public int getNotificationIconID() {
        return android.R.drawable.ic_menu_call;
    }

    @Override
    public int getNotificationColorInt() {
        return Color.GREEN;
    }

    @Override
    public PendingIntent getPendingIntent(Context context)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mContent);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        return PendingIntent.getActivity(context,getNotificationId(),sendIntent,0);
    }

    public WhatsappAction(int notificationId,String taskName,String mContent) {
        super(notificationId,taskName);
        this.mContent = mContent;
    }
}
