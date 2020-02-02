package alonbd.merosh.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import alonbd.merosh.BackgroundAndroid.TasksManager;
import alonbd.merosh.R;

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

    fab.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AddTriggerActivity.class);
            startActivity(intent);

    });
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_burgermenu_white);


    nav.setNavigationItemSelectedListener(this);
    //        nav.setCheckedItem(R.id.order_asc);
    //        //}else{
    //        nav.setCheckedItem(R.id.order_des);
    //        //}
    recyclerView.setHasFixedSize(true);//?
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//TODO false is order
    recyclerView.setAdapter(new RecyclerViewAdapter(tasksManager.loadData()));
    //TODO callbacks and touchHelper
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

        if (menuItem.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddTriggerActivity.class);
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
