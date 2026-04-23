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
        
        // מבטל את הצביעה האוטומטית של האייקונים כדי שה-Selector יעבוד
        bottomNav.setItemIconTintList(null);
        
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

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

        // Check and schedule notifications defined in MasterActivity
        checkNotificationPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuCred) {
            Intent intent = new Intent(this, CreditsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuLogout) {
            FBRef.refAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
