package alonbd.simpler.BackgroundAndroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.Task;

public class BTReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TasksManager tm = TasksManager.getInstance(context);
        ArrayList<Task> data = tm.loadData();
        for(Task t : data) {
            if(t.getTrigger().matchIntent(intent))
                t.start(context);
        }
    }

}

