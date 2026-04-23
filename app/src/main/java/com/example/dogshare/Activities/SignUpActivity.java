package com.example.dogshare.Activities;

import static com.example.dogshare.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.User;
import com.example.dogshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import com.example.dogshare.MasterActivity;

public class SignUpActivity extends MasterActivity {

    TextInputEditText etUsername, etEmail, etPassword;
    RadioGroup rgUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        rgUserType = findViewById(R.id.rgUserType);
    }

    public void registerUser(View view) {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        int selectedId = rgUserType.getCheckedRadioButtonId();

        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || selectedId == -1) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRB = findViewById(selectedId);
        String userTypeRaw = selectedRB.getText().toString();
        final String userType = userTypeRaw.toLowerCase().replace(" ", "");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Creating Account");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        refAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                User newUser = new User(uid, username, userType, "");
                                FBRef.refUsers.child(uid).setValue(newUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> dbTask) {
                                                pd.dismiss();
                                                if (dbTask.isSuccessful()) {
                                                    Log.i("SignUpActivity", "User created and saved: " + userType);
                                                    Toast.makeText(SignUpActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

                                                    Intent intent;
                                                    if (userType.equals("dogwalker")) {
                                                        intent = new Intent(SignUpActivity.this, DogWalkerDetailsActivity.class);
                                                    } else if (userType.equals("parent") || userType.equals("child")) {
                                                        intent = new Intent(SignUpActivity.this, AppInfoActivity.class);
                                                    } else {
                                                        intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                    }
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Error saving user data", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            pd.dismiss();
                            FBRef.handleAuthError(SignUpActivity.this, task.getException());
                        }
                    }
                });
    }

    public void goToLogin(View view) {
        finish();
    }
}
