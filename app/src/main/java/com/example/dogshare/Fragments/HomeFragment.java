package com.example.dogshare.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.Group;
import com.example.dogshare.Objects.User;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView tvGreeting, tvGroupCodeValue;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvGreeting = view.findViewById(R.id.tv_greeting);
        tvGroupCodeValue = view.findViewById(R.id.tv_group_code_value);

        loadUserData();

        return view;
    }

    private void loadUserData() {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        FBRef.refUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvGreeting.setText("Hi " + user.getUserName() + "!");
                    
                    if (user.getGroupId() != null && !user.getGroupId().isEmpty()) {
                        loadGroupCode(user.getGroupId());
                    } else {
                        tvGroupCodeValue.setText("No Group");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGroupCode(String groupId) {
        FBRef.refGroups.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                if (group != null) {
                    tvGroupCodeValue.setText(group.getGroupCode());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading group code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
