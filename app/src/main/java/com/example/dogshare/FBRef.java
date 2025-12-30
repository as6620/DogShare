package com.example.dogshare;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class FBRef {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refData = FBDB.getReference("Data");

//    public static FirebaseStorage FBST = FirebaseStorage.getInstance();
//    public static StorageReference refST = FBST.getReference();
}