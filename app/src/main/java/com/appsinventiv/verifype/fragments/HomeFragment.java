package com.appsinventiv.verifype.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsinventiv.verifype.Activites.CompleteProfile;
import com.appsinventiv.verifype.Activites.LatestFrauds;
import com.appsinventiv.verifype.Activites.PlayGame;
import com.appsinventiv.verifype.Activites.PsychologyQuestions;
import com.appsinventiv.verifype.Adapters.MainSliderAdapter;
import com.appsinventiv.verifype.Models.BannerModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Activites.ReportScreen;
import com.appsinventiv.verifype.Activites.VerifyScreen;
import com.appsinventiv.verifype.Utils.Constants;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private View rootView;
    LinearLayout verify, report, latestFraud;
    TextView userName;
    ImageView close;
    RelativeLayout warning;
    DatabaseReference mDatabase;
    ArrayList<BannerModel> sliderList = new ArrayList<>();
    LinearLayout playGame;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        userName = rootView.findViewById(R.id.userName);
        report = rootView.findViewById(R.id.report);
        playGame = rootView.findViewById(R.id.playGame);
        warning = rootView.findViewById(R.id.warning);
        close = rootView.findViewById(R.id.close);
        verify = rootView.findViewById(R.id.verify);
        latestFraud = rootView.findViewById(R.id.latestFraud);
        userName.setText("Welcome, " + (SharedPrefs.getUser().getName()==null?"":SharedPrefs.getUser().getName()));
        if (SharedPrefs.getUser().getEmail() == null && SharedPrefs.getUser().getCity() == null) {
            warning.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.GONE);
        }
        mDatabase = Constants.M_DATABASE;
        getBannersFromDb();
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CompleteProfile.class));
            }
        });
        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PsychologyQuestions.class));
            }
        });
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warning.setVisibility(View.GONE);
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

    private void getBannersFromDb() {
        mDatabase.child("Banners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    BannerModel model = snapshot1.getValue(BannerModel.class);
                    sliderList.add(model);

                }
                setUpViewPager();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpViewPager() {
        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        MainSliderAdapter adapter = new MainSliderAdapter(getContext(), sliderList);

        viewPager.setAdapter(adapter);

        DotsIndicator dotsIndicator = rootView.findViewById(R.id.dotsIndicator);
        viewPager.setOffscreenPageLimit(1);
        dotsIndicator.setViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}