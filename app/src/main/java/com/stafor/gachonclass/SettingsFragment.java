package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    TextView nameText, versionText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_settings, container, false);

        MainActivity activity = (MainActivity) getActivity();

        nameText = (TextView) rootView.findViewById(R.id.tv_name);
        versionText = (TextView) rootView.findViewById(R.id.tv_version);

        return rootView;
    }
}
