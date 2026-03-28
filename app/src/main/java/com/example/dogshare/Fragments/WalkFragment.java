package com.example.dogshare.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.dogshare.R;

public class WalkFragment extends Fragment {

    private TextView[][] walkSchedule = new TextView[7][3];

    public WalkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk, container, false);

        // יום ראשון
        walkSchedule[0][0] = view.findViewById(R.id.tvSunMorn);
        walkSchedule[0][1] = view.findViewById(R.id.tvSunAfter);
        walkSchedule[0][2] = view.findViewById(R.id.tvSunEve);
        // יום שני
        walkSchedule[1][0] = view.findViewById(R.id.tvMonMorn);
        walkSchedule[1][1] = view.findViewById(R.id.tvMonAfter);
        walkSchedule[1][2] = view.findViewById(R.id.tvMonEve);
        // יום שלישי
        walkSchedule[2][0] = view.findViewById(R.id.tvTueMorn);
        walkSchedule[2][1] = view.findViewById(R.id.tvTueAfter);
        walkSchedule[2][2] = view.findViewById(R.id.tvTueEve);
        // יום רביעי
        walkSchedule[3][0] = view.findViewById(R.id.tvWedMorn);
        walkSchedule[3][1] = view.findViewById(R.id.tvWedAfter);
        walkSchedule[3][2] = view.findViewById(R.id.tvWedEve);
        // יום חמישי
        walkSchedule[4][0] = view.findViewById(R.id.tvThuMorn);
        walkSchedule[4][1] = view.findViewById(R.id.tvThuAfter);
        walkSchedule[4][2] = view.findViewById(R.id.tvThuEve);
        // יום שישי
        walkSchedule[5][0] = view.findViewById(R.id.tvFriMorn);
        walkSchedule[5][1] = view.findViewById(R.id.tvFriAfter);
        walkSchedule[5][2] = view.findViewById(R.id.tvFriEve);
        // יום שבת
        walkSchedule[6][0] = view.findViewById(R.id.tvSatMorn);
        walkSchedule[6][1] = view.findViewById(R.id.tvSatAfter);
        walkSchedule[6][2] = view.findViewById(R.id.tvSatEve);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                if (walkSchedule[i][j] != null) {
                    walkSchedule[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeWalker(v);
                        }
                    });
                }
            }
        }

        return view;
    }

    public void changeWalker(View v) {
        final TextView clickedTV = (TextView) v;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Who's walking the dog?");

        final String[] names = {"Idan", "Yuval", "Adva"};

        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedTV.setText(names[which]);
            }
        });

        builder.setNegativeButton("CANCEL", null);

        builder.setNeutralButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedTV.setText("");
            }
        });

        builder.show();
    }
}