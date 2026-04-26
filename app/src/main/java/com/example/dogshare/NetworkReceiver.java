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
    private AlertDialog networkDialog;
    private Activity activity;

    public NetworkReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null && activeNetwork.isConnected());

        if (!isConnected) {
            showDialog();
        } else {
            dismissDialog();
        }
    }

    private void showDialog() {
        if ((networkDialog == null || !networkDialog.isShowing()) && activity != null && !activity.isFinishing()) {
            networkDialog = new AlertDialog.Builder(activity)
                    .setTitle("Connection Lost")
                    .setMessage("DogShare requires internet connection. Please reconnect.")
                    .setCancelable(false)
                    .setPositiveButton("Settings", (dialog, which) -> {
                        try {
                            Intent wifiIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                            wifiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(wifiIntent);
                        } catch (Exception e) {
                            // Fallback to general settings if WIFI settings intent fails
                            Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(settingsIntent);
                        }
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