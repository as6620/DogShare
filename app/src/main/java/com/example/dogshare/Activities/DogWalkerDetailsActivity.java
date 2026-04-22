package com.example.dogshare.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.dogshare.FBRef;
import com.example.dogshare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DogWalkerDetailsActivity extends AppCompatActivity {

    private EditText etFullName, etAge, etPhone, etCity, etAddress, etExperience;
    private static final int REQUEST_IMAGE_CHOOSER = 111;
    private Uri cameraUri;
    private Uri imageUriToUpload;

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
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            String filename = "walker_" + System.currentTimeMillis();
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(filename, ".jpg", storageDir);
            cameraUri = FileProvider.getUriForFile(this, "com.example.dogshare.fileprovider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        } catch (IOException e) {
            cameraUri = null;
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Profile Photo");
        if (cameraUri != null) {
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        }
        startActivityForResult(chooserIntent, REQUEST_IMAGE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CHOOSER && resultCode == RESULT_OK) {
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

    public void onNextClicked(View view) {
        String fullName = etFullName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();

        if (fullName.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || city.isEmpty() || address.isEmpty() || experience.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (imageUriToUpload == null) {
            Toast.makeText(this, "Please upload a profile photo", Toast.LENGTH_SHORT).show();
            return;
        }

        saveDataWithImage(fullName, Integer.parseInt(ageStr), phone, city, address, experience);
    }

    private void saveDataWithImage(String name, int age, String phone, String city, String address, String exp) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading profile...");
        pd.setCancelable(false);
        pd.show();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference refFile = FBRef.refProfileImages.child("users/" + uid + ".jpg");

        refFile.putFile(imageUriToUpload).continueWithTask(task -> {
            if (!task.isSuccessful()) throw task.getException();
            return refFile.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String downloadUrl = task.getResult().toString();
                Map<String, Object> updates = new HashMap<>();
                updates.put("userName", name);
                updates.put("age", age);
                updates.put("phoneNum", phone);
                updates.put("city", city);
                updates.put("address", address);
                updates.put("experienceWithDogs", exp);
                updates.put("profilePhotoUrl", downloadUrl);

                FBRef.refUsers.child(uid).updateChildren(updates).addOnCompleteListener(dbTask -> {
                    pd.dismiss();
                    if (dbTask.isSuccessful()) {
                        startActivity(new Intent(this, NeedDogWalker.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Database update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                pd.dismiss();
                Toast.makeText(this, "Photo upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}