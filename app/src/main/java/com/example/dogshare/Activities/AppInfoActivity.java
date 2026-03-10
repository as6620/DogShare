package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogshare.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    public void createGroup(View view) {
        Intent intent = new Intent(AppInfoActivity.this, DogInfoActivity.class);
        startActivity(intent);
    }

    public void joinGroup(View view) {
        // Implementation for joining a group will be added here
    }

    public void allowNotifications(View view) {
        // Implementation for allowing notifications will be added here
    }
}
