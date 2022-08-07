package com.appsinventiv.verifype.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appsinventiv.verifype.Activites.LatestFrauds;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Activites.ReportScreen;
import com.appsinventiv.verifype.Activites.VerifyScreen;
import com.appsinventiv.verifype.Utils.SharedPrefs;

public class HomeFragment extends Fragment {


    private View rootView;
    LinearLayout verify, report, latestFraud;
    TextView userName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        userName = rootView.findViewById(R.id.userName);
        report = rootView.findViewById(R.id.report);
        verify = rootView.findViewById(R.id.verify);
        latestFraud = rootView.findViewById(R.id.latestFraud);
        userName.setText("Welcome, " + SharedPrefs.getUser().getName());
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VerifyScreen.class));
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReportScreen.class));
            }
        });
        latestFraud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LatestFrauds.class));
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}