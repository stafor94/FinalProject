package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener{
    TextView nameText, versionText;
    Button profileBtn;
    String name = "홍길동";

    final int REQUEST_CODE = 1001;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_settings, container, false);

        nameText = (TextView) rootView.findViewById(R.id.tv_name);
        versionText = (TextView) rootView.findViewById(R.id.tv_version);
        profileBtn = (Button) rootView.findViewById(R.id.btn_profile);
        profileBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile) {
            Intent myIntent = new Intent(getContext(), ProfileActivity.class);
            startActivityForResult(myIntent, REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                name = data.getStringExtra("name");
                nameText.setText(name);
            }
        }
    }
}
