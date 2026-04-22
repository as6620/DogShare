    package com.example.dogshare.Activities;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Environment;
    import android.provider.MediaStore;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.RadioGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    import androidx.core.content.FileProvider;

    import com.example.dogshare.FBRef;
    import com.example.dogshare.Objects.Dog;
    import com.example.dogshare.Objects.Group;
    import com.example.dogshare.R;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;

    import java.io.File;
    import java.io.IOException;
    import java.util.Random;

    public class DogInfoActivity extends AppCompatActivity {

        EditText etDogName, etAge, etDogBreed;
        RadioGroup rgGender;
        CheckBox cbPublicDog;

        private static final int REQUEST_CAMERA_PERMISSION = 100;
        private static final int REQUEST_IMAGE_CHOOSER = 111;
        private Uri cameraUri;
        private Uri imageUriToUpload;

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

                    if (imageUriToUpload == null) {
                        Toast.makeText(DogInfoActivity.this, "Please upload a photo of your dog", Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onResume() {
            super.onResume();
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }

        public void UploadDogPhoto(View view) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;

            try {
                String filename = "dog_" + System.currentTimeMillis();
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(filename, ".jpg", storageDir);

                cameraUri = FileProvider.getUriForFile(
                        this,
                        "com.example.dogshare.fileprovider",
                        photoFile
                );
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create file for camera", Toast.LENGTH_SHORT).show();
                cameraUri = null;
            }

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Dog Photo");
            if (cameraUri != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
            }

            startActivityForResult(chooserIntent, REQUEST_IMAGE_CHOOSER);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CHOOSER && resultCode == RESULT_OK) {
                imageUriToUpload = null;
                if (data != null && data.getData() != null) {
                    imageUriToUpload = data.getData();
                } else if (cameraUri != null) {
                    imageUriToUpload = cameraUri;
                }

                if (imageUriToUpload != null) {
                    Toast.makeText(this, "Photo selected!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void uploadDogImage(Uri uri, String dogId) {
            if (uri != null) {
                StorageReference refFile = FBRef.refProfileImages.child("dog_photos/" + dogId + ".jpg");
                refFile.putFile(uri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return refFile.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String imageUrl = downloadUri.toString();

                        // Update the dog object with the actual Download URL
                        FBRef.refDogs.child(dogId).child("dogPhotoUrl").setValue(imageUrl);
                        Log.i("DogInfoActivity", "Dog photo URL saved: " + imageUrl);
                    } else {
                        Log.e("DogInfoActivity", "Failed to get download URL");
                    }
                });
            }
        }

        private void generateUniqueGroupCodeAndSave(String dogName, int age, String breed, boolean gender, boolean shareDog) {
            String groupCode = String.format("%06d", new Random().nextInt(1000000));

            FBRef.refGroups.orderByChild("groupCode").equalTo(groupCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        generateUniqueGroupCodeAndSave(dogName, age, breed, gender, shareDog);
                    } else {
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
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Saving data...");
            pd.show();

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
                    dogId + ".jpg" // שם הקובץ ב-Storage
            );

            if (groupId != null && dogId != null) {
                FBRef.refGroups.child(groupId).setValue(newGroup);
                FBRef.refDogs.child(dogId).setValue(dog);

                if (uid != null) {
                    FBRef.refUsers.child(uid).child("groupId").setValue(groupId);
                }

                // העלאת התמונה
                uploadDogImage(imageUriToUpload, dogId);

                pd.dismiss();
                Intent intent = new Intent(DogInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }