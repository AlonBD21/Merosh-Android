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
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Task;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    NavigationView nav;
    CoordinatorLayout coordLayout;
    DrawerLayout rootDrawer;
    Toolbar toolbar;
    FloatingActionButton fab;
    TasksManager tasksManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        rootDrawer = findViewById(R.id.drawer_root);
        nav = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycler);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        coordLayout = findViewById(R.id.coord_layout);
        tasksManager = TasksManager.getInstance(this);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);

        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_burgermenu_black);


        nav.setNavigationItemSelectedListener(this);
        //        nav.setCheckedItem(R.id.order_asc);
        //        //}else{
        //        nav.setCheckedItem(R.id.order_des);
        //        //}
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//TODO false is order
        recyclerView.setAdapter(new RecyclerViewAdapter(this));
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
                        tm.removeTaskAt(position);
                        ((RecyclerViewAdapter)recyclerView.getAdapter()).RefreshData(MainActivity.this);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                    }).setNeutralButton("Cancel", (dialog, which) -> {
                        int position = viewHolder.getAdapterPosition();
                        dialog.dismiss();
                        recyclerView.getAdapter().notifyItemChanged(position);
                    }).setMessage("Are you sure you want to delete this Task?").setTitle("Task Deletion").setCancelable(true).setOnCancelListener(dialog -> {
                        int position = viewHolder.getAdapterPosition();
                        dialog.dismiss();
                        recyclerView.getAdapter().notifyItemChanged(position);
                    })
                            .create().show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            rootDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(menuItem.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }/*TODO Fix
        if (menuItem.getItemId() == R.id.order_asc) {
            nav.setCheckedItem(R.id.order_asc);
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
*/
        rootDrawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
