package alonbd.simpler.BackgroundAndroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import alonbd.simpler.TaskLogic.Task;

public class BtReceiver extends BroadcastReceiver {

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

