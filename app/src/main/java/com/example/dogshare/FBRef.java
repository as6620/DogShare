package com.example.dogshare;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBRef {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refData = FBDB.getReference("Data");
    public static DatabaseReference refUsers = FBDB.getReference("Users");
    public static DatabaseReference refDogs = FBDB.getReference("Dogs");
    public static DatabaseReference refGroups = FBDB.getReference("Groups");

    public static void handleAuthError(Context context, Exception exp) {
        if (exp instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(context, "Password too weak", Toast.LENGTH_LONG).show();
        } else if (exp instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
        } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(context, "Invalid email", Toast.LENGTH_LONG).show();
        } else if (exp instanceof FirebaseNetworkException) {
            Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        }
    }
}