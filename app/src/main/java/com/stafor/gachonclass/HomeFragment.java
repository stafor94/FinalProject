package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    Button lookupBtn, bookingBtn, classBtn, recommendBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        lookupBtn = (Button) rootView.findViewById(R.id.btn_lookup);
        lookupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "강의실 조회를 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        bookingBtn = (Button) rootView.findViewById(R.id.btn_booking);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "강의실 예약을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        classBtn = (Button) rootView.findViewById(R.id.btn_class);
        classBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "수업 조회를 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        recommendBtn = (Button) rootView.findViewById(R.id.btn_recommend);
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "건의사항을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
