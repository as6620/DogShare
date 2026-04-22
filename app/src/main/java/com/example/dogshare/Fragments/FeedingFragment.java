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

public class FeedingFragment extends Fragment {
    private TextView[][] feeder = new TextView[7][2];

    public FeedingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feeding, container, false);

        feeder[0][0] = view.findViewById(R.id.tV1);
        feeder[0][1] = view.findViewById(R.id.tV2);
        feeder[1][0] = view.findViewById(R.id.tV3);
        feeder[1][1] = view.findViewById(R.id.tV4);
        feeder[2][0] = view.findViewById(R.id.tV5);
        feeder[2][1] = view.findViewById(R.id.tV6);
        feeder[3][0] = view.findViewById(R.id.tV7);
        feeder[3][1] = view.findViewById(R.id.tV8);
        feeder[4][0] = view.findViewById(R.id.tV9);
        feeder[4][1] = view.findViewById(R.id.tV10);
        feeder[5][0] = view.findViewById(R.id.tV11);
        feeder[5][1] = view.findViewById(R.id.tV12);
        feeder[6][0] = view.findViewById(R.id.tV13);
        feeder[6][1] = view.findViewById(R.id.tV14);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                if (feeder[i][j] != null) {
                    feeder[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeFeederDialog(v);
                        }
                    });
                }
            }
        }

        return view;
    }

    public void changeFeederDialog(View v) {
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
