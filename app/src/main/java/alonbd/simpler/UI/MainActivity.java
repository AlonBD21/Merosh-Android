package alonbd.simpler.UI;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;

import alonbd.simpler.BackgroundAndroid.LocationService;
import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Task;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "ThugMainActivity";
    private RecyclerView mRecyclerView;
    private NavigationView mNav;
    private DrawerLayout mRootDrawerLayout;
    private FloatingActionButton mFab;
    private CoordinatorLayout mCoordinatorLayout;
    private boolean mDefaultOrder;
    private ImageView mMenuFlipButton;
    private boolean mLockFlipBtn;
    private RecyclerViewAdapter mAdapter;
    private Comparator<Task> mComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mRootDrawerLayout = findViewById(R.id.drawer_root);

        mRecyclerView = findViewById(R.id.recycler);
        mFab = findViewById(R.id.fab);

        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_burgermenu);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RecyclerViewAdapter(TasksManager.getInstance(this).getData());
        Task.setRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.END) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setPositiveButton("Yes, Remove", (dialog, which) -> {
                        dialog.dismiss();
                        TasksManager tm = TasksManager.getInstance(MainActivity.this);
                        int position = viewHolder.getAdapterPosition();
                        Task removed = tm.getData().remove(position);
                        tm.saveData();
                        mRecyclerView.getAdapter().notifyItemRemoved(position);
                        Intent intent = new Intent(MainActivity.this, LocationService.class);
                        startService(intent);
                        Snackbar.make(mCoordinatorLayout, "The task '" + removed.getName() + "' has been removed.", Snackbar.LENGTH_LONG).
                                setAction("Cancel", (v) -> {
                                    tm.getData().add(position, removed);
                                    mRecyclerView.getAdapter().notifyItemInserted(position);
                                    Snackbar.make(mCoordinatorLayout, "The task '" + removed.getName() + "' has been added back.", Snackbar.LENGTH_LONG).show();
                                }).show();
                    }).setNeutralButton("Cancel", (dialog, which) -> {
                        int position = viewHolder.getAdapterPosition();
                        dialog.dismiss();
                        mRecyclerView.getAdapter().notifyItemChanged(position);
                    }).setMessage("Are you sure you want to delete this Task?").setTitle("Task Deletion").setCancelable(true).setOnCancelListener(dialog -> {
                        int position = viewHolder.getAdapterPosition();
                        dialog.dismiss();
                        mRecyclerView.getAdapter().notifyItemChanged(position);
                    })
                            .create().show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);

        });

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

        String newName = getIntent().getStringExtra(AddActionActivity.NEW_TASK_NAME_EXTRA_STRING);
        if(newName != null) {
            Snackbar.make(mCoordinatorLayout, "A task named '" + newName + "' has been added successfully.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(mRootDrawerLayout.isDrawerOpen(GravityCompat.START))
                mRootDrawerLayout.closeDrawer(GravityCompat.START);
            else mRootDrawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mNav = findViewById(R.id.nav_view);
        mNav.setCheckedItem(R.id.by_date);
        mNav.setNavigationItemSelectedListener(this);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar_options, menu);
        mDefaultOrder = false;

        mMenuFlipButton = (ImageView) menu.findItem(R.id.order_flip).getActionView();
        mMenuFlipButton.setImageResource(R.drawable.ic_chevron_down);
        mLockFlipBtn = false;

        mMenuFlipButton.setOnClickListener((v -> {
            if(mLockFlipBtn) return;
            mLockFlipBtn = true;
            Animation animation;
            mDefaultOrder = !mDefaultOrder;
            if(mDefaultOrder) {
                animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
            }else{
                animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
            }
            updateOrder(mNav.getCheckedItem().getItemId());
            animation.setFillAfter(true);
            animation.setDuration(300);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mLockFlipBtn = false;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mMenuFlipButton.startAnimation(animation);
        }));
        updateOrder(mNav.getCheckedItem().getItemId());
        return true;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
        mRootDrawerLayout.closeDrawer(GravityCompat.START);
        updateOrder(menuItem.getItemId());
        return true;
    }

    private void updateOrder(int checkedOrderByID) {
        switch(checkedOrderByID) {
            case R.id.by_name:
                mComparator = new Task.NameComparator(mDefaultOrder);
                break;
            case R.id.by_status:
                mComparator = new Task.StatusComparator(mDefaultOrder);
                break;
            case R.id.by_trigger:
                mComparator = new Task.TriggerComparator(mDefaultOrder);
                break;
            default:
                mComparator = new Task.DefaultDateComparator(mDefaultOrder);


        }
        TasksManager.getInstance(this).orderBy(mComparator);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        Task.removeRecyclerViewAdapter();
    }
}
