package alonbd.simpler.BackgroundAndroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.Task;

public class BluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TasksManager.startAllWithIntent(context,intent);
    }

}

