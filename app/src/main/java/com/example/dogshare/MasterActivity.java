package com.example.dogshare;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MasterActivity extends AppCompatActivity {

    // Variable to hold the network listener
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the receiver and connect it to this screen
        networkReceiver = new NetworkReceiver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for internet connection changes
        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, networkFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to save battery and prevent errors
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }
}