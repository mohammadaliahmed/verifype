package com.appsinventiv.verifype.Activites;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsinventiv.verifype.ContactsFragment;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.appsinventiv.verifype.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private Fragment fragment;

    private BottomNavigationView navigation;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("");

        navigation = (BottomNavigationView) findViewById(R.id.customBottomBar);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        initNavigationDrawer();
        fragment = new HomeFragment();
        loadFragment(fragment);
        updateFcmKey();

    }
    private void updateFcmKey() {
        if (SharedPrefs.getUser() != null) {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    String token = task.getResult();
                    SharedPrefs.setFcmKey(token);
                    mDatabase = FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();

                    mDatabase.child("Users").child(SharedPrefs.getUser().getPhone()).child("fcmKey").setValue(token);

                }
            });
        }
    }

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:


                    fragment = new HomeFragment();
                    loadFragment(fragment);


                    return true;
                case R.id.navigation_contact:
                    fragment = new ContactsFragment();
                    loadFragment(fragment);

                    return true;
//                case R.id.navigation_premium:
//                    fragment = new PremiumFragment();
//                    loadFragment(fragment);
//
//                    return true;
//                case R.id.navigation_block:
////                    fragment = new FAQsFragment();
//                    loadFragment(fragment);
//                    return true;


            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void initNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
//        TextView navSubtitle = (TextView) headerView.findViewById(R.id.phone_drawer);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {

            Toast.makeText(this, "notificatiobs", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_language) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}