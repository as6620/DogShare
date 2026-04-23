package com.example.dogshare.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.dogshare.FBRef;
import com.example.dogshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MeetupAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Dog> dogs;

    public MeetupAdapter(Context context, ArrayList<Dog> dogs) {
        this.context = context;
        this.dogs = dogs;
    }

    @Override
    public int getCount() { return dogs.size(); }

    @Override
    public Object getItem(int position) { return dogs.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meetup_item, parent, false);
        }

        Dog currentDog = dogs.get(position);

        TextView tvName = convertView.findViewById(R.id.tvMeetupDogName);
        TextView tvBreed = convertView.findViewById(R.id.tvMeetupDogBreed);
        TextView tvLocation = convertView.findViewById(R.id.tvMeetupDistance);
        ImageView ivDog = convertView.findViewById(R.id.ivMeetupDogPicture);
        Button btnSchedule = convertView.findViewById(R.id.btnScheduleMeetup);

        tvName.setText("Dog name: " + currentDog.getDogName());
        tvBreed.setText("Breed: " + currentDog.getDogBreed());

        // Fetch location (city) from the owner
        FBRef.refUsers.orderByChild("groupId").equalTo(currentDog.getGroupId()).limitToFirst(1)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot userSnap : snapshot.getChildren()) {
                        String city = userSnap.child("city").getValue(String.class);
                        tvLocation.setText("Location: " + (city != null ? city : "Unknown"));
                    }
                }
                @Override public void onCancelled(DatabaseError error) {}
            });

        if (currentDog.getDogPhotoUrl() != null && !currentDog.getDogPhotoUrl().isEmpty()) {
            Glide.with(context).load(currentDog.getDogPhotoUrl()).placeholder(R.drawable.ic_launcher_background).into(ivDog);
        }

        btnSchedule.setOnClickListener(v -> {
            // Logic to schedule a meetup
        });

        return convertView;
    }
}