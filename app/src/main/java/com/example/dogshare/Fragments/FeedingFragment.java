package com.example.dogshare.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.User;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FeedingFragment extends Fragment {

    private TextView[][] feedingTable = new TextView[7][2];
    private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] mealTimes = {"Morn", "After"};
    
    private boolean isEditMode = false;
    private boolean isUserParent = false;
    private String currentUserName = "";
    private ArrayList<String> groupMemberNames = new ArrayList<>();
    private String userGroupId = "";

    public FeedingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeding, container, false);

        initTable(view);
        checkUserRoleAndFetchGroup();

        Button btnEdit = view.findViewById(R.id.btnEditParents);
        btnEdit.setOnClickListener(v -> {
            if (!isUserParent) {
                Toast.makeText(getContext(), "Only parents can edit the feeding schedule", Toast.LENGTH_LONG).show();
                return;
            }
            
            isEditMode = !isEditMode;
            btnEdit.setText(isEditMode ? "LOCK TABLE" : "Edit only for parents");
            Toast.makeText(getContext(), isEditMode ? "Edit mode enabled" : "Table locked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void initTable(View view) {
        int[] resIds = {
            R.id.tV1, R.id.tV2, R.id.tV3, R.id.tV4, R.id.tV5, R.id.tV6, R.id.tV7,
            R.id.tV8, R.id.tV9, R.id.tV10, R.id.tV11, R.id.tV12, R.id.tV13, R.id.tV14
        };

        int count = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                feedingTable[i][j] = view.findViewById(resIds[count++]);
                feedingTable[i][j].setOnClickListener(v -> onCellClicked(v));
            }
        }
    }

    private void checkUserRoleAndFetchGroup() {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        FBRef.refUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    userGroupId = user.getGroupId();
                    currentUserName = user.getUserName();
                    isUserParent = "parent".equalsIgnoreCase(user.getRole());
                    
                    if (userGroupId != null && !userGroupId.isEmpty()) {
                        loadGroupMembers(userGroupId);
                        listenToFeedingChanges(userGroupId);
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadGroupMembers(String groupId) {
        FBRef.refUsers.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupMemberNames.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("userName").getValue(String.class);
                    if (name != null && !groupMemberNames.contains(name)) {
                        groupMemberNames.add(name);
                    }
                }
                if (currentUserName != null && !currentUserName.isEmpty() && !groupMemberNames.contains(currentUserName)) {
                    groupMemberNames.add(0, currentUserName);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenToFeedingChanges(String groupId) {
        FBRef.refGroups.child(groupId).child("feedingTracker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 2; j++) {
                        String key = days[i] + mealTimes[j];
                        String name = snapshot.child(key).getValue(String.class);
                        feedingTable[i][j].setText(name != null ? name : "");
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void onCellClicked(View v) {
        if (!isEditMode) {
            Toast.makeText(getContext(), isUserParent ? "Enable edit mode first" : "Only parents can edit", Toast.LENGTH_SHORT).show();
            return;
        }

        int row = -1, col = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                if (feedingTable[i][j].getId() == v.getId()) {
                    row = i; col = j; break;
                }
            }
        }

        final int finalRow = row, finalCol = col;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Feeder");

        String[] names = groupMemberNames.toArray(new String[0]);
        builder.setItems(names, (dialog, which) -> saveCellToFirebase(finalRow, finalCol, names[which]));
        builder.setNeutralButton("RESET", (dialog, which) -> saveCellToFirebase(finalRow, finalCol, ""));
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    private void saveCellToFirebase(int row, int col, String name) {
        if (userGroupId == null || userGroupId.isEmpty()) return;
        String key = days[row] + mealTimes[col];
        FBRef.refGroups.child(userGroupId).child("feedingTracker").child(key).setValue(name);
    }
}