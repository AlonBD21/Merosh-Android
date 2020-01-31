package alonbd.merosh.TaskLogic;

import android.bluetooth.BluetoothDevice;

public class BTTrigger extends Trigger {
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
}
