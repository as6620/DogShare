package com.example.dogshare.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogshare.R;

import com.example.dogshare.MasterActivity;

public class CreditsActivity extends MasterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Credits");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}