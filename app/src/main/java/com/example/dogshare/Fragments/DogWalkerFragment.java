package com.example.dogshare.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dogshare.Objects.DogWalker;
import com.example.dogshare.Objects.WalkerAdapter;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DogWalkerFragment extends Fragment {

    private ListView lvWalkers;
    private ArrayList<DogWalker> walkerList;
    private WalkerAdapter adapter;
    private DatabaseReference dbRef;

    public DogWalkerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_walker, container, false);

        lvWalkers = view.findViewById(R.id.lvWalkers);
        walkerList = new ArrayList<>();
        adapter = new WalkerAdapter(getContext(), walkerList);
        lvWalkers.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // Fetching users where role is "dogwalker"
        dbRef.orderByChild("role").equalTo("dogwalker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                walkerList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DogWalker walker = postSnapshot.getValue(DogWalker.class);
                    if (walker != null) {
                        walkerList.add(walker);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        return view;
    }
}