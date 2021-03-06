package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.regex.Pattern;

import alonbd.simpler.R;

public class EmailAction extends IntentAction implements Serializable {
    private String mTo;
    private String mSubject;
    private String mContent;

    @Override
    public String getNotificationContentString(Context context) {
        return context.getString(R.string.email_notif_content, mTo, mContent);
    }

    @Override
    public String getNotificationTitleString(Context context) {
        return context.getString(R.string.email_notif_title);
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
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,splitAddresses(mTo));
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
    private static String[] splitAddresses(String addresses){
        Pattern r = Pattern.compile("[,\\s]+");
        return r.split(addresses);
    }

    @Override
    public View getDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_email, null);
        ((TextView) view.findViewById(R.id.to_tv)).setText(mTo);
        ((TextView) view.findViewById(R.id.subject_tv)).setText(mSubject);
        ((TextView) view.findViewById(R.id.content_tv)).setText(mContent);
        return view;
    }
}
