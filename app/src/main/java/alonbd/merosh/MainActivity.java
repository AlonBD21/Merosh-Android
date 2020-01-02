package alonbd.merosh;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    NavigationView nav;
    CoordinatorLayout coordLayout;
    DrawerLayout rootDrawer;
    Toolbar toolbar;
    FloatingActionButton fab;
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Link To Mission Add Activity
                //Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
                //startActivity(intent);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu);

        //if (dataManager.getOrderAsc()){ TODO, check order
            nav.setCheckedItem(R.id.order_asc);
        //}else{
            nav.setCheckedItem(R.id.order_des);;
        //}

        nav.setNavigationItemSelectedListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //TODO Add recycler adapter callbacks and touchhelper
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            rootDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        /*TODO Fix
        if (menuItem.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
            startActivity(intent);
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        //TODO, fix
        //((ContactsRecyclerAdapter) recyclerView.getAdapter()).dataChange(dataManager.getContacts());
        //recyclerView.getAdapter().notifyDataSetChanged();
    }
}
