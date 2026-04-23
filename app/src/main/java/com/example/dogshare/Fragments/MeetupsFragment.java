package com.example.dogshare.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.Dog;
import com.example.dogshare.Objects.MeetupAdapter;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MeetupsFragment extends Fragment {

    private ListView lvMeetups;
    private ArrayList<Dog> dogList;
    private MeetupAdapter adapter;
    private Switch swFilter;
    private TextView tvMeetupSubtitle;
    private String myDogBreed = "";

    public MeetupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetups, container, false);

        lvMeetups = view.findViewById(R.id.lvMeetups);
        swFilter = view.findViewById(R.id.swFilter);
        tvMeetupSubtitle = view.findViewById(R.id.tvMeetupSubtitle);
        
        dogList = new ArrayList<>();
        adapter = new MeetupAdapter(getContext(), dogList);
        lvMeetups.setAdapter(adapter);

        // First, find my dog's breed to use for filtering later
        fetchMyDogBreed();

        // Listen for switch changes
        swFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvMeetupSubtitle.setText("Same Breed Dogs");
            } else {
                tvMeetupSubtitle.setText("Nearby Dogs");
            }
            loadMeetupDogs(isChecked);
        });

        loadMeetupDogs(false); // Default: Location (all public dogs)

        return view;
    }

    private void fetchMyDogBreed() {
        String uid = FBRef.refAuth.getUid();
        if (uid == null) return;

        // Get user's groupId first
        FBRef.refUsers.child(uid).child("groupId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String groupId = snapshot.getValue(String.class);
                if (groupId != null) {
                    // Get the dog's breed from this group
                    FBRef.refDogs.orderByChild("groupId").equalTo(groupId).limitToFirst(1)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dogSnapshot) {
                                    for (DataSnapshot ds : dogSnapshot.getChildren()) {
                                        Dog dog = ds.getValue(Dog.class);
                                        if (dog != null) {
                                            myDogBreed = dog.getDogBreed();
                                        }
                                    }
                                }
                                @Override public void onCancelled(@NonNull DatabaseError error) {}
                            });
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadMeetupDogs(boolean filterByBreed) {
        FBRef.refDogs.orderByChild("shareDogToOthers").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Dog dog = postSnapshot.getValue(Dog.class);
                    if (dog != null) {
                        // If breed filter is ON, only add dogs with same breed
                        if (filterByBreed) {
                            if (dog.getDogBreed().equalsIgnoreCase(myDogBreed)) {
                                dogList.add(dog);
                            }
                        } else {
                            // Location mode: add everyone who is public
                            dogList.add(dog);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}