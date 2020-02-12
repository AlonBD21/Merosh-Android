package alonbd.simpler.UI;

import android.app.Notification;
import android.os.Bundle;
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

import com.google.android.material.tabs.TabLayout;

import alonbd.simpler.TaskLogic.BTTrigger;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.Trigger;
import alonbd.simpler.R;

public class AddTriggerActivity extends AppCompatActivity {
    ImageButton button;
    ViewPager viewPager;
    TabLayout tabLayout;
    TaskBuilder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrigger);
        button = findViewById(R.id.next_btn);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        tabLayout.setupWithViewPager(viewPager);
        builder = (TaskBuilder) getIntent().getSerializableExtra(TaskBuilder.EXTRA_TAG);

        button.setOnClickListener((View v) -> {
            builder.setTrigger(makeTrigger());
            AddActionActivity.preDialog(AddTriggerActivity.this,builder);
        });
    }

    class TabsPagerAdapter extends FragmentPagerAdapter {
        private final String[] TAB_TITLES =
                new String[]{"Bluetooth", "WiFi", "Location"};

        public TabsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new BTTriggerFragment();
                case 1:
                    return new WiFiTriggerFragment();
                case 2:
                    return new LocationTriggerFragment();
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
            return 3;
        }
    }

    public Trigger makeTrigger() {
        int tabIndex = viewPager.getCurrentItem();
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
                Toast.makeText(this, "checked id is " + radioGroup.getCheckedRadioButtonId(), Toast.LENGTH_LONG).show();
                return new BTTrigger(connection.isChecked(), disconnection.isChecked(), radioButton.getTag().toString(), radioButton.getText().toString());
            default:
                return null;

        }
    }

}
