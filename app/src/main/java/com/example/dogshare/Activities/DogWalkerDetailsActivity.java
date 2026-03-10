package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogshare.R;

public class DogWalkerDetailsActivity extends AppCompatActivity {

    private EditText etFullName, etAge, etPhone, etCity, etAddress, etExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_walker_details);
        initViews();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        etExperience = findViewById(R.id.etExperience);
    }

    public void onUploadPhotoClicked(View view) {
        Toast.makeText(this, "Upload Photo Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onNextClicked(View view) {
        String fullName = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();

        if (fullName.isEmpty() || age.isEmpty() || phone.isEmpty() || city.isEmpty() || address.isEmpty() || experience.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

//        Intent intent = new Intent(this, DogDashboardActivity.class);
//        intent.putExtra("FULL_NAME", fullName);
//        intent.putExtra("AGE", age);
//        intent.putExtra("PHONE", phone);
//        intent.putExtra("CITY", city);
//        intent.putExtra("ADDRESS", address);
//        intent.putExtra("EXPERIENCE", experience);
//        startActivity(intent);
//        finish();
    }
}