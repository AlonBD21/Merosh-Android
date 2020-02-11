package alonbd.simpler.TaskLogic;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;

public class BTTrigger extends Trigger implements Serializable {
    private boolean onConnection;
    private boolean onDisconnection;
    private String deviceAdress;
    private String deviceName;


    public BTTrigger(boolean onConnection, boolean onDisconnect,String deviceAdress, String deviceName) {
        this.onConnection = onConnection;
        this.onDisconnection = onDisconnect;
        this.deviceAdress = deviceAdress;
        this.deviceName =  deviceName;
    }

    @Override
    public boolean matchIntent(Intent intent) {
        String action = intent.getAction();
        if(onConnection && BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(deviceAdress)) return true;
        } else if(onDisconnection && BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(deviceAdress) )return true;
        }
        return false;
    }
}
