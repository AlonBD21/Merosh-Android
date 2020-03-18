package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import java.io.Serializable;

import alonbd.simpler.R;

public class EmailAction extends IntentAction implements Serializable {
    private String mTo;
    private String mSubject;
    private String mContent;

    @Override
    public String getNotificationContentString() {
        return "To "+mSubject+" About "+mSubject;
    }

    @Override
    public String getNotificationTitleString() {
        return "Click to send E-Mail";
    }


    @Override
    public int getNotificationIconID() {
        return R.drawable.ic_notification_email;
    }

    @Override
    public int getNotificationColorInt() {
        return Color.RED;
    }

    @Override
    public PendingIntent getPendingIntent(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",mTo, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mContent);
        return PendingIntent.getActivity(context,getNotificationId(),Intent.createChooser(emailIntent, "Send email..."),0);
    }

    public EmailAction(int notificationId,String taskName,String mTo, String mSubject, String mContent) {
        super(notificationId,taskName);
        this.mTo = mTo;
        this.mSubject = mSubject;
        this.mContent = mContent;
    }
}
