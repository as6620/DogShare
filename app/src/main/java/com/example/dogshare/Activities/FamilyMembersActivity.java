package com.example.dogshare.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dogshare.FBRef;
import com.example.dogshare.MasterActivity;
import com.example.dogshare.Objects.User;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyMembersActivity extends MasterActivity {

    private ListView lvFamilyMembers;
    private ArrayList<String> memberNames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_members);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Family Members");
        }

        lvFamilyMembers = findViewById(R.id.lvFamilyMembers);
        memberNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberNames);
        lvFamilyMembers.setAdapter(adapter);

        loadFamilyMembers();
    }

    private void loadFamilyMembers() {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        // First get the user's groupId
        FBRef.refUsers.child(uid).child("groupId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String groupId = snapshot.getValue(String.class);
                if (groupId != null && !groupId.isEmpty()) { // בדיקה אם המשתמש אכן שייך לקבוצה
                    fetchUsersInGroup(groupId);
                } else {
                    Toast.makeText(FamilyMembersActivity.this, "You are not in a group yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchUsersInGroup(String groupId) {
        FBRef.refUsers.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memberNames.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null) {
                        String displayText = user.getUserName() + " (" + user.getRole() + ")";
                        memberNames.add(displayText);
                    }
                }
                adapter.notifyDataSetChanged(); // הנתונים השתנו וצריך לרענן את המסך
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() { //חץ חזור
        onBackPressed();
        return true;
    }
}
