package alonbd.simpler.TaskLogic;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;

import alonbd.simpler.R;

public class BluetoothTrigger extends Trigger implements Serializable {
    private boolean mOnConnection;
    private boolean mOnDisconnection;
    private String mDeviceAddress;
    private String mDeviceName;

    public BluetoothTrigger(boolean mSingleUse, boolean mOnConnection, boolean mOnDisconnection, String mDeviceName, String mDeviceAddress) {
        super(mSingleUse);
        this.mOnConnection = mOnConnection;
        this.mOnDisconnection = mOnDisconnection;
        this.mDeviceAddress = mDeviceAddress;
        this.mDeviceName = mDeviceName;
    }
    @Override
    public boolean matchCondition(Object object) {
        if(object instanceof Intent) {
            Intent intent = (Intent) object;
            String action = intent.getAction();
            if(mOnConnection && BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                return device.getAddress().equals(mDeviceAddress);
            } else if(mOnDisconnection && BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                return device.getAddress().equals(mDeviceAddress);
            }
            return false;
        }
        return false;
    }

    @Override
    protected View getTypeDescriptiveView(Context context) {
        View view = View.inflate(context, R.layout.layout_view_bluetooth, null);
        ((TextView) view.findViewById(R.id.device_tv)).setText(mDeviceName);
        ((CheckBox) view.findViewById(R.id.con_cb)).setChecked(mOnConnection);
        ((CheckBox) view.findViewById(R.id.dis_cb)).setChecked(mOnDisconnection);

        return view;
    }
}
