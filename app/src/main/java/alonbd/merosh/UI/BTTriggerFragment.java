package alonbd.merosh.UI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;

import alonbd.merosh.R;
import alonbd.merosh.TaskLogic.BTTrigger;
import alonbd.merosh.TaskLogic.Trigger;

public class BTTriggerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trigger_bt, container, false);
        radioGroup = root.findViewById(R.id.radio_group);
        btErr = root.findViewById(R.id.bt_err_tv);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            updateDeviceList();
        } else {
            btErr.setText("Please turn on bluetooth to see list");
        }
        btOnReciever = new BroadcastReceiver() {
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
        getContext().registerReceiver(btOnReciever, btOnFilter);
    }

    BroadcastReceiver btOnReciever;
    RadioGroup radioGroup;
    TextView btErr;

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
            radioGroup.addView(newButton, lp);
        }
        btErr.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(btOnReciever);
    }

}
