package alonbd.simpler.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

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
    private boolean mOrderUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mRootDrawerLayout = findViewById(R.id.drawer_root);
        mNav = findViewById(R.id.nav_view);
        mRecyclerView = findViewById(R.id.recycler);
        mFab = findViewById(R.id.fab);
        mOrderUp = false;

        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_burgermenu);
        mNav.setNavigationItemSelectedListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//TODO false is order
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(TasksManager.getInstance(this).getData(), getResources());
        Task.setRecyclerViewAdapter(recyclerViewAdapter);
        mRecyclerView.setAdapter(recyclerViewAdapter);
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
            Log.d(TAG, "onCreate: New task name is not null");
            Snackbar.make(mCoordinatorLayout, "A task named '" + newName + "' has been added successfully.", Snackbar.LENGTH_LONG).show();
        } else
            Log.d(TAG, "onCreate: New task name is null");

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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar_options, menu);


        ImageButton flipBtn = ((ImageButton) menu.findItem(R.id.order_flip).getActionView());
        int scale = (int) Resources.getSystem().getDisplayMetrics().density;
        flipBtn.setImageResource(R.drawable.ic_chevron_up);
        flipBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flipBtn.setOnClickListener((v -> {
            ImageButton ib = (ImageButton) v;
            if(mOrderUp){
                ib.setImageResource(R.drawable.ic_chevron_down);
            }else{
                ib.setImageResource(R.drawable.ic_chevron_up);
            }
            mOrderUp = !mOrderUp;
            Animation animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
            animation.setDuration(250);
            animation.setInterpolator(new DecelerateInterpolator());
            ib.startAnimation(animation);
        }));
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(menuItem.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }

   /*     if (menuItem.getItemId() == R.id.order_asc) {
            mNav.setCheckedItem(R.id.order_asc);
            dataManager.sort(true);
            recyclerView.getAdapter().notifyDataSetChanged();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataManager.sort(true);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }).run();
            Snackbar.make(coordLayout,"Order Changed",Snackbar.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.order_des) {
            menuItem.setChecked(true);
            nav.setCheckedItem(R.id.order_des);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataManager.sort(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }).run();
            Snackbar.make(coordLayout,"Order Changed",Snackbar.LENGTH_SHORT).show();}
        mRootDrawerLayout.closeDrawer(GravityCompat.START);*/
        mRootDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        Task.removeRecyclerViewAdapter();
    }
}
