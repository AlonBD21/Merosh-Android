package alonbd.simpler.TaskLogic;

import android.content.Intent;

import java.io.Serializable;

public class WiFiTrigger extends Trigger implements Serializable {
    private boolean onConnection;
    private boolean onDisconnection;
    private String deviceAdress;
    private String deviceName;

    public WiFiTrigger(boolean onConnection, boolean onDisconnect,String deviceAdress, String deviceName) {
        this.onConnection = onConnection;
        this.onDisconnection = onDisconnect;
        this.deviceAdress = deviceAdress;
        this.deviceName =  deviceName;
    }

    @Override
    public boolean matchIntent(Intent intent) {
        String action = intent.getAction();
        /*NetworkInfo
        if(onConnection && BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
             device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(deviceAdress)) return true;
        } else if(onDisconnection && BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(deviceAdress) )return true;
        }*/
        return false;
    }
}
