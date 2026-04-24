package com.example.dogshare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogshare.FBRef;
import com.example.dogshare.Objects.Dog;
import com.example.dogshare.Objects.DogAdapter;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import com.example.dogshare.MasterActivity;

public class NeedDogWalker extends MasterActivity {

    private ListView lvDogs;
    private ArrayList<Dog> dogList;
    private DogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_dog_walker);

        lvDogs = findViewById(R.id.lvDogs);
        dogList = new ArrayList<>();
        adapter = new DogAdapter(this, dogList);
        lvDogs.setAdapter(adapter);

        loadDogsThatNeedWalker();
    }

    private void loadDogsThatNeedWalker() {
        // Query dogs where shareDogToOthers is true
        FBRef.refDogs.orderByChild("shareDogToOthers").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Dog dog = postSnapshot.getValue(Dog.class);
                    if (dog != null) {
                        dogList.add(dog);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}