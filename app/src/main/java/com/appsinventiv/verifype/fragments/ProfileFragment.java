package com.appsinventiv.verifype.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appsinventiv.verifype.Activites.ChatScreen;
import com.appsinventiv.verifype.R;

public class ProfileFragment extends Fragment {
    private View rootView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
