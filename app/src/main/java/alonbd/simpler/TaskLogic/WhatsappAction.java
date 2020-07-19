package alonbd.simpler.TaskLogic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import alonbd.simpler.R;

public class WhatsappAction extends IntentAction implements Serializable {
    private String mContent;
    private String mTo;

    @Override
    public String getNotificationContentString(Context context) {
        return mContent;
    }

    @Override
    public String getNotificationTitleString(Context context) {
        return context.getString(R.string.wa_click_to_send);
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

    @Override
    public View getDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_whatsapp, null);
        ((TextView) view.findViewById(R.id.to_tv)).setText(mTo);
        ((TextView) view.findViewById(R.id.content_tv)).setText(mContent);
        return view;
    }
}
