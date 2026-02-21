package com.example.dogshare.Activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dogshare.Fragments.PlaceholderFragment;
import com.example.dogshare.Fragments.fragmentHome;
import com.example.dogshare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        // מבטל את הצביעה האוטומטית של האייקונים כדי שה-Selector יעבוד
        bottomNav.setItemIconTintList(null);
        
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_walking) {
                    selectedFragment = PlaceholderFragment.newInstance("Walking");
                } else if (itemId == R.id.nav_feeding) {
                    selectedFragment = PlaceholderFragment.newInstance("Feeding");
                } else if (itemId == R.id.nav_home) {
                    selectedFragment = new fragmentHome();
                } else if (itemId == R.id.nav_meetups) {
                    selectedFragment = PlaceholderFragment.newInstance("Meetups");
                } else if (itemId == R.id.nav_dogwalker) {
                    selectedFragment = PlaceholderFragment.newInstance("Dog Walker");
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
    }
}