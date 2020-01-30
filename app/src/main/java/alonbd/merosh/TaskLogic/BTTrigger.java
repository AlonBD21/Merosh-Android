package alonbd.merosh.TaskLogic;

import android.bluetooth.BluetoothDevice;

public class BTTrigger extends Trigger {
    private boolean onConnection;
    private boolean onDisconnect;
    private BluetoothDevice device;


    public BTTrigger(boolean onConnection, boolean onDisconnect, BluetoothDevice device) {
        this.onConnection = onConnection;
        this.onDisconnect = onDisconnect;
        this.device = device;
    }
}
