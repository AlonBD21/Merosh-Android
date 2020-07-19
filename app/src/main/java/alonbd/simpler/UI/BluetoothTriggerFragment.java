package alonbd.simpler.UI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.BluetoothTrigger;
import alonbd.simpler.TaskLogic.Trigger;

public class BluetoothTriggerFragment extends TriggerFragment {
    private BroadcastReceiver mBTTurnedOnReceiver;
    private RadioGroup mDeviceRadioGroup;
    private TextView mBtErrTv;
    private CheckBox mConnectionCb;
    private CheckBox mDisconnectionCb;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trigger_bt, container, false);
        mDeviceRadioGroup = root.findViewById(R.id.radio_group);
        mBtErrTv = root.findViewById(R.id.bt_err_tv);
        mConnectionCb = root.findViewById(R.id.connection_cb);
        mDisconnectionCb = root.findViewById(R.id.disconnection_cb);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            updateDeviceList();
        } else {
            mBtErrTv.setVisibility(View.VISIBLE);
        }
        mBTTurnedOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if(state == BluetoothAdapter.STATE_ON) {
                        updateDeviceList();
                    }
                }
            }
        };
        IntentFilter btOnFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getContext().registerReceiver(mBTTurnedOnReceiver, btOnFilter);
    }

    private void updateDeviceList() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> pairedDevices = new ArrayList<BluetoothDevice>();
        pairedDevices.addAll(set);

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lp.setMargins(10, 10, 10, 50);
        for(BluetoothDevice device : pairedDevices
        ) {
            RadioButton newButton = new RadioButton(this.getContext());
            newButton.setText(device.getName());
            newButton.setTag(device.getAddress());
            newButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            mDeviceRadioGroup.addView(newButton, lp);
        }
        mBtErrTv.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mBTTurnedOnReceiver);
    }

    @Override
    public Trigger genTrigger(boolean singleUse) {
        int id = mDeviceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = mDeviceRadioGroup.findViewById(id);
        if(!(mConnectionCb.isChecked() || mDisconnectionCb.isChecked())) {
            Toast.makeText(getContext(), getString(R.string.trigger_no_check_err), Toast.LENGTH_SHORT).show();
            return null;
        } else if(radioButton == null) {
            Toast.makeText(getContext(), getString(R.string.trigger_no_device_err), Toast.LENGTH_SHORT).show();
            return null;
        }
        return new BluetoothTrigger(singleUse, mConnectionCb.isChecked(), mDisconnectionCb.isChecked(), radioButton.getText().toString(), radioButton.getTag().toString());
    }
}
