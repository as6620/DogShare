package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.dogshare.FBRef;
import com.example.dogshare.Fragments.FeedingFragment;
import com.example.dogshare.Fragments.MeetupsFragment;
import com.example.dogshare.Fragments.DogWalkerFragment;
import com.example.dogshare.Fragments.HomeFragment;
import com.example.dogshare.Fragments.WalkFragment;
import com.example.dogshare.MasterActivity;
import com.example.dogshare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends MasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        bottomNav.setItemIconTintList(null);
        
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                // מונע טעינה מחדש אם המשתמש כבר נמצא באותו מסך
                if (itemId == bottomNav.getSelectedItemId() && getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                    return true;
                }

                Fragment selectedFragment = null;

                if (itemId == R.id.nav_walking) {
                    selectedFragment = new WalkFragment();
                } else if (itemId == R.id.nav_feeding) {
                    selectedFragment = new FeedingFragment();
                } else if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_meetups) {
                    selectedFragment = new MeetupsFragment();
                } else if (itemId == R.id.nav_dogwalker) {
                    selectedFragment = new DogWalkerFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        checkNotificationPermission();
    }
}
