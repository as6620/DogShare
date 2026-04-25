package com.example.dogshare.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dogshare.FBRef;
import com.example.dogshare.MasterActivity;
import com.example.dogshare.Objects.Group;
import com.example.dogshare.Objects.User;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AppInfoActivity extends MasterActivity {

    private Button btnCreateGroup, btnJoinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnJoinGroup = findViewById(R.id.btnJoinGroup);

        checkUserRoleAndSetupUI();
    }

    private void checkUserRoleAndSetupUI() {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        FBRef.refUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    // בדיקת תפקיד המשתמש (הורה/ילד/אחר)
                    if ("parent".equalsIgnoreCase(user.getRole())) {
                        btnCreateGroup.setVisibility(View.VISIBLE);
                        btnJoinGroup.setVisibility(View.VISIBLE);
                    } else if ("child".equalsIgnoreCase(user.getRole())) {
                        btnCreateGroup.setVisibility(View.GONE);
                        btnJoinGroup.setVisibility(View.VISIBLE);
                    } else {
                        // For dogwalker or others, maybe hide both or handle differently
                        btnCreateGroup.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void createGroup(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Create New Family Group")
                .setMessage("Are you sure you want to create a new group? This will make you the parent and allow you to add family members and your dog.")
                .setPositiveButton("Yes, Create", (dialog, which) -> {
                    Intent intent = new Intent(AppInfoActivity.this, DogInfoActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void joinGroup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Join a Group");
        builder.setMessage("Enter the 6-digit group code:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = input.getText().toString().trim();
                if (code.length() == 6) {
                    processJoinRequest(code);
                } else {
                    Toast.makeText(AppInfoActivity.this, "Please enter a valid 6-digit code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void processJoinRequest(String code) {
        FBRef.refGroups.orderByChild("groupCode").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                        Group group = groupSnapshot.getValue(Group.class);
                        if (group != null) {
                            addUserToGroup(group);
                            return;
                        }
                    }
                } else {
                    Toast.makeText(AppInfoActivity.this, "Group not found. Please check the code.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppInfoActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToGroup(Group group) {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        // 1. Update Group user list
        if (group.getUserIds() == null) {
            group.setUserIds(new java.util.ArrayList<String>());
        }
        if (!group.getUserIds().contains(uid)) {
            group.getUserIds().add(uid);
        }
        FBRef.refGroups.child(group.getGroupId()).setValue(group);

        // 2. Update User's group reference
        FBRef.refUsers.child(uid).child("groupId").setValue(group.getGroupId());

        Toast.makeText(this, "Joined group successfully!", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(AppInfoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //תנקה את כל המסכים שמעליו ותחזור לMAIN
        startActivity(intent);
        finish();
    }

    public void allowNotifications(View view) {
        checkNotificationPermission();
    }
}
