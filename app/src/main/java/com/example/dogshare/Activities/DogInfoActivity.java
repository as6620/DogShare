package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.Dog;
import com.example.dogshare.R;

public class DogInfoActivity extends AppCompatActivity {

    EditText etDogName, etAge, etDogBreed;
    RadioGroup rgGender;
    CheckBox cbPublicDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        etDogName = findViewById(R.id.etDogName);
        etAge = findViewById(R.id.etAge);
        etDogBreed = findViewById(R.id.etDogBreed);
        rgGender = findViewById(R.id.rgGender);
        cbPublicDog = findViewById(R.id.cbPublicDog);

        Button btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dogName = etDogName.getText().toString().trim();
                String ageStr = etAge.getText().toString().trim();
                String breed = etDogBreed.getText().toString().trim();

                if (dogName.isEmpty() || ageStr.isEmpty() || breed.isEmpty()) {
                    Toast.makeText(DogInfoActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);

                int selectedGender = rgGender.getCheckedRadioButtonId();
                if (selectedGender == -1) {
                    Toast.makeText(DogInfoActivity.this, "Please select dog gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean gender = (selectedGender == R.id.rbMale);
                Boolean shareDog = cbPublicDog.isChecked();

                // יצירת מזהה ייחודי לכלב
                String dogId = FBRef.refDogs.push().getKey();
                
                // שימוש ב-UID של המשתמש המחובר כ-GroupId ראשוני (או כל לוגיקה אחרת)
                String uid = FBRef.refAuth.getUid();

                Dog dog = new Dog(
                        dogId,
                        uid != null ? uid : "no_group",
                        dogName,
                        age,
                        breed,
                        gender,
                        shareDog,
                        "" // Photo URL
                );

                if (dogId != null) {
                    FBRef.refDogs.child(dogId).setValue(dog)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DogInfoActivity.this, "Dog saved successfully!", Toast.LENGTH_SHORT).show();
                                    // מעבר למסך הבא (למשל MainActivity)
                                    Intent intent = new Intent(DogInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(DogInfoActivity.this, "Error saving dog", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
