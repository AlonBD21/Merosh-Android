package alonbd.simpler.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;

import alonbd.simpler.BuildConfig;
import alonbd.simpler.TaskLogic.BluetoothTrigger;
import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.Trigger;
import alonbd.simpler.R;

public class AddTriggerActivity extends AppCompatActivity {
    private static final String TAG = "ThugAddTriggerActivity";

    private ImageButton mButton;
    private ViewPager mViewPager;
    private TabLayout mTabsLayout;
    private TaskBuilder mBuilder;

    private BluetoothTriggerFragment bluetoothTriggerFragment;
    private WifiTriggerFragment wifiTriggerFragment;
    private LocationTriggerFragment locationTriggerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrigger);
        mButton = findViewById(R.id.next_btn);
        mViewPager = findViewById(R.id.view_pager);
        mTabsLayout = findViewById(R.id.tabs);
        mViewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabsLayout.setupWithViewPager(mViewPager);
        mBuilder = (TaskBuilder) getIntent().getSerializableExtra(TaskBuilder.EXTRA_TAG);


        mButton.setOnClickListener((View v) -> {
            mBuilder.setTrigger(makeTrigger());
            AddActionActivity.preDialog(AddTriggerActivity.this, mBuilder);
        });

    }

    class TabsPagerAdapter extends FragmentPagerAdapter {
        private final String[] TAB_TITLES =
                new String[]{"Bluetooth", "Location","WiFi" };

        public TabsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    if(bluetoothTriggerFragment == null)
                        bluetoothTriggerFragment = new BluetoothTriggerFragment();
                    return bluetoothTriggerFragment;
        //        case 2:
       //             if(wifiTriggerFragment == null) wifiTriggerFragment = new WifiTriggerFragment();
        //            return wifiTriggerFragment;
                case 1:
                    if(locationTriggerFragment == null)
                        locationTriggerFragment = new LocationTriggerFragment();
                    return locationTriggerFragment;
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }

        @Override
        public int getCount() {
            //return 3;
            return 2;
        }
    }

    public Trigger makeTrigger() {
        int tabIndex = mViewPager.getCurrentItem();
        switch(tabIndex) {
            case 0:
                RadioGroup radioGroup = findViewById(R.id.radio_group);
                CheckBox connection = findViewById(R.id.connection_cb);
                CheckBox disconnection = findViewById(R.id.disconnection_cb);
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroup.findViewById(id);
                if(!(connection.isChecked() || disconnection.isChecked())) {
                    Toast.makeText(this, "At least one checkbox must be checked", Toast.LENGTH_SHORT).show();
                    return null;
                } else if(radioButton == null) {
                    Toast.makeText(this, "Please choose a device", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return new BluetoothTrigger(connection.isChecked(), disconnection.isChecked(), radioButton.getText().toString(),radioButton.getTag().toString());
            case 1:
                Marker marker = locationTriggerFragment.getMarker();
                if(marker == null) {
                    Toast.makeText(this, "Put a marker on the map to selecet activation location", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return new LocationTrigger(marker.getPosition());
            default:
                return null;

        }
    }

}
