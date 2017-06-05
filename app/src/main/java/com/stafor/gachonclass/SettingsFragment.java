package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    public static boolean boolVibe, boolSound;
    private DBHelper_Profile dbHelper;
    TextView nameText, majorText, versionText;
    Button profileBtn, resetBtn;
    Switch vibeSwitch, soundSwitch;
    int intVibe, intSound;

    final int REQUEST_CODE = 1001;  // 프로필 입력 요청코드

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_settings, container, false);

        dbHelper = new DBHelper_Profile(getContext());

        nameText = (TextView) rootView.findViewById(R.id.tv_name);
        majorText = (TextView) rootView.findViewById(R.id.tv_major);
        versionText = (TextView) rootView.findViewById(R.id.tv_version);
        profileBtn = (Button) rootView.findViewById(R.id.btn_profile);
        resetBtn = (Button) rootView.findViewById(R.id.btn_reset);
        profileBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        boolSound = dbHelper.printData(4).equals("1") ? true:false; // 1이면 true 아니면 false
        boolVibe = dbHelper.printData(5).equals("1") ? true:false;  // 1이면 true 아니면 false
        intVibe = (boolVibe ? 1:0);
        intSound = (boolSound ? 1:0);

        // 소리 스위치
        soundSwitch = (Switch) rootView.findViewById(R.id.switch_sound);
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolSound = isChecked;
                intSound = (boolSound ? 1:0);
                dbHelper.update(intSound, intVibe); // Profile DB의 정보 수정
            }
        });
        // 진동 스위치
        vibeSwitch = (Switch) rootView.findViewById(R.id.switch_vibe);
        vibeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolVibe = isChecked;
                intVibe = (boolVibe ? 1:0);
                dbHelper.update(intSound, intVibe); // Profile DB의 정보 수정
            }
        });

        // DB에 사용자의 이름이 있으면 Profile DB에서 스위치 정보를 받아와서 설정한다
        if (!MainActivity.name.equals("")) {
            soundSwitch.setChecked(boolSound);
            vibeSwitch.setChecked(boolVibe);
        }
        nameText.setText(MainActivity.name);
        majorText.setText(MainActivity.major);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile) {
            Intent myIntent = new Intent(getContext(), ProfileActivity.class);
            startActivityForResult(myIntent, REQUEST_CODE);
        } else if (v.getId() == R.id.btn_reset) {
            getContext().deleteDatabase("USERDB.db");
            Toast.makeText(getContext(), "앱을 재실행해 주세요.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                nameText.setText(MainActivity.name);
                majorText.setText(MainActivity.major);
            }
        }
    }
}
