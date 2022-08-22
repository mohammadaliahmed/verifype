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
import com.appsinventiv.verifype.Adapters.MainSliderAdapter;
import com.appsinventiv.verifype.Models.BannerModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Activites.ReportScreen;
import com.appsinventiv.verifype.Activites.VerifyScreen;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private View rootView;
    LinearLayout verify, report, latestFraud;
    TextView userName;
    ImageView close;
    RelativeLayout warning;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        userName = rootView.findViewById(R.id.userName);
        report = rootView.findViewById(R.id.report);
        warning = rootView.findViewById(R.id.warning);
        close = rootView.findViewById(R.id.close);
        verify = rootView.findViewById(R.id.verify);
        latestFraud = rootView.findViewById(R.id.latestFraud);
        userName.setText("Welcome, " + SharedPrefs.getUser().getName());
        if (SharedPrefs.getUser().getEmail() == null && SharedPrefs.getUser().getCity() == null) {
            warning.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.GONE);
        }
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CompleteProfile.class));
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
        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        ArrayList<BannerModel> sliderList = new ArrayList<>();
        sliderList.add(new BannerModel("sdas", "https://www.worcester.gov.uk/images/easyblog_shared/2020/Scam-alert-shutterstock_1012719211.jpg", "text1"));
        sliderList.add(new BannerModel("sdas", "https://www.worcester.gov.uk/images/easyblog_shared/2020/Scam-alert-shutterstock_1012719211.jpg", "text2"));
        sliderList.add(new BannerModel("sdas", "https://www.worcester.gov.uk/images/easyblog_shared/2020/Scam-alert-shutterstock_1012719211.jpg", "text3"));
        MainSliderAdapter adapter = new MainSliderAdapter(getContext(), sliderList);

        viewPager.setAdapter(adapter);

        DotsIndicator dotsIndicator = rootView.findViewById(R.id.dotsIndicator);
        viewPager.setOffscreenPageLimit(1);
        dotsIndicator.setViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}