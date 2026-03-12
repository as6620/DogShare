package com.example.dogshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * BroadcastReceiver that monitors network connectivity changes.
 * It displays an alert dialog when the internet connection is lost.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static boolean isConnected = false;
    private static AlertDialog networkDialog;
    private Activity activity;

    /**
     * Constructor that receives the Activity context.
     * This is required to display the UI dialog on the correct screen.
     * * @param activity The current active activity.
     */
    public NetworkReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // Update connection status
        isConnected = (activeNetwork != null && activeNetwork.isConnected());

        if (!isConnected) {
            showDialog();
        } else {
            dismissDialog();
        }
    }

    /**
     * Creates and displays a non-cancelable dialog informing the user
     * that the internet connection has been lost.
     */
    private void showDialog() {
        // Check if the dialog is already showing and ensure the activity is still valid/running
        if ((networkDialog == null || !networkDialog.isShowing()) && activity != null && !activity.isFinishing()) {
            networkDialog = new AlertDialog.Builder(activity)
                    .setTitle("Connection Lost")
                    .setMessage("DogShare requires internet connection. Please reconnect.")
                    .setCancelable(false) // Prevents the user from dismissing the dialog without internet
                    .setPositiveButton("Settings", (dialog, which) -> {
                        // Redirect the user to the device Wi-Fi settings
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    })
                    .create();
            networkDialog.show();
        }
    }

    /**
     * Dismisses the network warning dialog if it is currently visible.
     */
    private void dismissDialog() {
        if (networkDialog != null && networkDialog.isShowing()) {
            networkDialog.dismiss();
            networkDialog = null;
        }
    }
}