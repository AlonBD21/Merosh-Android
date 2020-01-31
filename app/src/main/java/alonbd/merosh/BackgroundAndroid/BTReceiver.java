package alonbd.merosh.BackgroundAndroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import alonbd.merosh.TaskLogic.Task;

public class BTReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Toast.makeText(context, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
        } else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Toast.makeText(context, "Disconnected from " + device.getName(), Toast.LENGTH_SHORT).show();
        }
        TasksManager tm = TasksManager.getInstance(context);
        ArrayList<Task> data = tm.loadData();


    }

}

