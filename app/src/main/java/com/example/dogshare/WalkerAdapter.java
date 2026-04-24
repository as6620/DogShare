package com.example.dogshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dogshare.Objects.DogWalker;

import java.util.ArrayList;

public class WalkerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DogWalker> walkers;

    public WalkerAdapter(Context context, ArrayList<DogWalker> walkers) {
        this.context = context;
        this.walkers = walkers;
    }

    @Override
    public int getCount() {
        return walkers.size();
    }

    @Override
    public Object getItem(int position) {
        return walkers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.walker_item, parent, false);
        }

        DogWalker currentWalker = walkers.get(position);

        TextView tvName = convertView.findViewById(R.id.tvWalkerName);
        TextView tvAge = convertView.findViewById(R.id.tvWalkerAge);
        TextView tvExp = convertView.findViewById(R.id.tvWalkerExp);
        ImageView ivProfile = convertView.findViewById(R.id.ivWalkerPicture);
        Button btnSelect = convertView.findViewById(R.id.btnSelectWalker);

        tvName.setText("Name: " + currentWalker.getUserName());
        tvAge.setText("Age: " + currentWalker.getAge());
        tvExp.setText("Experience: " + currentWalker.getExperienceWithDogs());

        if (currentWalker.getProfilePhotoUrl() != null && !currentWalker.getProfilePhotoUrl().isEmpty()) {
            Glide.with(context)
                    .load(currentWalker.getProfilePhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(android.R.drawable.ic_menu_report_image)
                    .centerCrop()
                    .into(ivProfile);
        } else {
            ivProfile.setImageResource(R.drawable.ic_launcher_background);
        }

        btnSelect.setOnClickListener(v -> {
            // Logic for selecting walker
        });

        return convertView;
    }
}