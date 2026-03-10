package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.Dog;
import com.example.dogshare.Objects.Group;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

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

                boolean gender = (selectedGender == R.id.rbMale);
                boolean shareDog = cbPublicDog.isChecked();

                generateUniqueGroupCodeAndSave(dogName, age, breed, gender, shareDog);
            }
        });
    }

    private void generateUniqueGroupCodeAndSave(String dogName, int age, String breed, boolean gender, boolean shareDog) {
        String groupCode = String.format("%06d", new Random().nextInt(1000000));

        // בדיקה האם הקוד כבר קיים ב-Firebase
        FBRef.refGroups.orderByChild("groupCode").equalTo(groupCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // אם הקוד קיים, נגריל קוד חדש ונבדוק שוב
                    generateUniqueGroupCodeAndSave(dogName, age, breed, gender, shareDog);
                } else {
                    // אם הקוד לא קיים, אפשר לשמור
                    saveGroupAndDog(dogName, age, breed, gender, shareDog, groupCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DogInfoActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveGroupAndDog(String dogName, int age, String breed, boolean gender, boolean shareDog, String groupCode) {
        String groupId = FBRef.refGroups.push().getKey();
        Group newGroup = new Group(groupId, groupCode);
        String uid = FBRef.refAuth.getUid();
        if (uid != null) {
            newGroup.getUserIds().add(uid);
        }

        String dogId = FBRef.refDogs.push().getKey();
        newGroup.getDogIds().add(dogId);

        Dog dog = new Dog(
                dogId,
                groupId,
                dogName,
                age,
                breed,
                gender,
                shareDog,
                "" // Photo URL
        );

        if (groupId != null && dogId != null) {
            FBRef.refGroups.child(groupId).setValue(newGroup);
            FBRef.refDogs.child(dogId).setValue(dog);

            if (uid != null) {
                FBRef.refUsers.child(uid).child("groupId").setValue(groupId);
            }

            Intent intent = new Intent(DogInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
