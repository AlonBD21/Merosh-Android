package alonbd.simpler.TaskLogic;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.io.Serializable;

public class BtTrigger extends Trigger implements Serializable {
    private boolean mOnConnection;
    private boolean mOnDisconnection;
    private String mDeviceAddress;
    private String mDeviceName;


    public BtTrigger(boolean mOnConnection, boolean mOnDisconnection, String mDeviceName, String mDeviceAddress) {
        this.mOnConnection = mOnConnection;
        this.mOnDisconnection = mOnDisconnection;
        this.mDeviceAddress = mDeviceAddress;
        this.mDeviceName = mDeviceName;
    }

    @Override
    public boolean matchIntent(Intent intent) {
        String action = intent.getAction();
        if(mOnConnection && BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(mDeviceAddress)) return true;
        } else if(mOnDisconnection && BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getAddress().equals(mDeviceAddress) )return true;
        }
        return false;
    }
}
