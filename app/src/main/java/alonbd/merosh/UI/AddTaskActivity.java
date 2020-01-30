package alonbd.merosh.UI;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import alonbd.merosh.R;

public class AddTaskActivity extends AppCompatActivity {
    private final String FRAGMENTTAG_BT = "bluetooth";
    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addtask);
            ViewPager viewPager = findViewById(R.id.view_pager);
            TabLayout tabLayout = findViewById(R.id.tabs);

            viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
            tabLayout.setupWithViewPager(viewPager);
    }
    public class TabsPagerAdapter extends FragmentPagerAdapter {
        private final String[] TAB_TITLES =
                new String[] { "Bluetooth", "WiFi", "Location" };

        public TabsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
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
}
