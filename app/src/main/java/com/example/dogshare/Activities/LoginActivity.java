package com.example.dogshare.Activities;

import static com.example.dogshare.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogshare.FBRef;
import com.example.dogshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    EditText eTEmail, eTPass;
    CheckBox cbStayLogin;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTEmail = findViewById(R.id.eTEmail);
        eTPass  = findViewById(R.id.eTPass);
        cbStayLogin = findViewById(R.id.cbStayLogin);

        settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean stayConnect = settings.getBoolean("stayConnect", false);

        if (refAuth.getCurrentUser() != null && stayConnect) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void loginUser(View view) {
        String email = eTEmail.getText().toString();
        String pass  = eTPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Logging in...");
            pd.setCancelable(false);
            pd.show();

            refAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();

                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("stayConnect", cbStayLogin.isChecked());
                                editor.apply();

                                Log.i("LoginActivity", "signIn:success");
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                FBRef.handleAuthError(LoginActivity.this, task.getException());
                            }
                        }
                    });
        }
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}