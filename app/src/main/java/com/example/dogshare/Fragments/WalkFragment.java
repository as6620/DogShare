package com.example.dogshare.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
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
import java.util.HashMap;
import java.util.Map;

public class WalkFragment extends Fragment {

    private TextView[][] walkSchedule = new TextView[7][3];
    private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] times = {"Morn", "After", "Eve"};
    
    private boolean isEditMode = false;
    private boolean isUserParent = false;
    private String currentUserName = "";
    private ArrayList<String> groupMemberNames = new ArrayList<>();
    private String userGroupId = "";

    public WalkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk, container, false);

        initTable(view);
        checkUserRoleAndFetchGroup();

        Button btnEdit = view.findViewById(R.id.btnEditParents);
        btnEdit.setOnClickListener(v -> {
            if (!isUserParent) {
                Toast.makeText(getContext(), "Only parents can edit the schedule", Toast.LENGTH_LONG).show();
                return;
            }
            
            isEditMode = !isEditMode;
            btnEdit.setText(isEditMode ? "LOCK TABLE" : "Edit only for parents");
            Toast.makeText(getContext(), isEditMode ? "Edit mode enabled" : "Table locked", Toast.LENGTH_SHORT).show();
        });

        Button btnAskReplacement = view.findViewById(R.id.btnAskReplacement);
        btnAskReplacement.setOnClickListener(v -> askForReplacement());

        return view;
    }

    private void askForReplacement() {
        ArrayList<String> myShifts = new ArrayList<>();
        ArrayList<Integer> shiftRows = new ArrayList<>();
        ArrayList<Integer> shiftCols = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                String val = walkSchedule[i][j].getText().toString();
                if (val.equals(currentUserName)) {
                    myShifts.add(days[i] + " " + times[j]);
                    shiftRows.add(i);
                    shiftCols.add(j);
                }
            }
        }

        if (myShifts.isEmpty()) {
            Toast.makeText(getContext(), "You have no walks scheduled", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a walk to replace");
        builder.setItems(myShifts.toArray(new String[0]), (dialog, which) -> {
            int r = shiftRows.get(which);
            int c = shiftCols.get(which);
            requestReplacement(r, c);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void requestReplacement(int row, int col) {
        if (userGroupId == null || userGroupId.isEmpty()) return;
        String key = days[row] + times[col];
        String helpValue = "HELP: " + currentUserName;
        FBRef.refGroups.child(userGroupId).child("walkSchedule").child(key).setValue(helpValue);
        
        // סימולציית הודעה לקבוצה (ניתן להוסיף כאן לוגיקה של Notifications בעתיד)
        Toast.makeText(getContext(), "Replacement request sent to group!", Toast.LENGTH_LONG).show();
    }

    private void initTable(View view) {
        int[] resIds = {
            R.id.tvSunMorn, R.id.tvSunAfter, R.id.tvSunEve,
            R.id.tvMonMorn, R.id.tvMonAfter, R.id.tvMonEve,
            R.id.tvTueMorn, R.id.tvTueAfter, R.id.tvTueEve,
            R.id.tvWedMorn, R.id.tvWedAfter, R.id.tvWedEve,
            R.id.tvThuMorn, R.id.tvThuAfter, R.id.tvThuEve,
            R.id.tvFriMorn, R.id.tvFriAfter, R.id.tvFriEve,
            R.id.tvSatMorn, R.id.tvSatAfter, R.id.tvSatEve
        };

        int count = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                walkSchedule[i][j] = view.findViewById(resIds[count++]);
                walkSchedule[i][j].setOnClickListener(v -> onCellClicked(v));
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
                        listenToScheduleChanges(userGroupId);
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
                // וודא שהמשתמש הנוכחי תמיד מופיע ברשימה
                if (currentUserName != null && !currentUserName.isEmpty() && !groupMemberNames.contains(currentUserName)) {
                    groupMemberNames.add(0, currentUserName); // הוספה לראש הרשימה
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenToScheduleChanges(String groupId) {
        FBRef.refGroups.child(groupId).child("walkSchedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 3; j++) {
                        String key = days[i] + times[j];
                        String name = snapshot.child(key).getValue(String.class);
                        if (name == null) name = "";
                        
                        walkSchedule[i][j].setText(name);
                        
                        if (name.startsWith("HELP:")) {
                            walkSchedule[i][j].setBackgroundColor(Color.parseColor("#FFCDD2")); // Light Red
                            walkSchedule[i][j].setTextColor(Color.RED);
                        } else {
                            walkSchedule[i][j].setBackgroundResource(R.drawable.table_border);
                            walkSchedule[i][j].setTextColor(Color.BLACK);
                        }
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void onCellClicked(View v) {
        int row = -1, col = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                if (walkSchedule[i][j].getId() == v.getId()) {
                    row = i; col = j; break;
                }
            }
        }
        
        final int finalRow = row, finalCol = col;
        String currentVal = walkSchedule[row][col].getText().toString();

        if (currentVal.startsWith("HELP:")) {
            showAcceptHelpDialog(finalRow, finalCol, currentVal);
            return;
        }

        if (!isEditMode) {
            Toast.makeText(getContext(), isUserParent ? "Enable edit mode first" : "Only parents can edit", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Walker");

        String[] names = groupMemberNames.toArray(new String[0]);
        builder.setItems(names, (dialog, which) -> saveCellToFirebase(finalRow, finalCol, names[which]));
        builder.setNeutralButton("RESET", (dialog, which) -> saveCellToFirebase(finalRow, finalCol, ""));
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    private void showAcceptHelpDialog(int row, int col, String currentVal) {
        String originalUser = currentVal.replace("HELP: ", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Help Needed!");
        builder.setMessage(originalUser + " cannot make this walk. Can you take it?");
        builder.setPositiveButton("I'LL TAKE IT", (dialog, which) -> {
            saveCellToFirebase(row, col, currentUserName);
            Toast.makeText(getContext(), "You took over the walk. Thanks!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Maybe later", null);
        builder.show();
    }

    private void saveCellToFirebase(int row, int col, String name) {
        if (userGroupId == null || userGroupId.isEmpty()) return;
        String key = days[row] + times[col];
        FBRef.refGroups.child(userGroupId).child("walkSchedule").child(key).setValue(name);
    }
}