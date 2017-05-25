package com.stafor.gachonclass;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class ClassFragment extends Fragment {
    GridLayout layout;
    String building, floor, classRoom;
    Button[] buttons;
    AlertDialog.Builder builder;
    final String[] items = {"수업정보 조회", "시간표 조회", "알림설정", "예약문의"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_class, container, false);
        layout = (GridLayout) rootView.findViewById(R.id.layout);
        init();

        // 다이어로그 생성 밑 설정
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(building + " " + floor);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), items[which] + " 선택!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        return rootView;
    }

    public void init() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            // 건물 이름과 층수를 받아온다.
            building = bundle.getString("building");
            floor = bundle.getString("floor");

            buttons = new Button[13];
            for (int i = 0; i < 13; i++) {
                buttons[i] = new Button(getContext());
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        classRoom = ((Button)v).getText().toString();
                        builder.setTitle(building + " "+ floor + "F " + classRoom + "호");
                        builder.show();
                    }
                });
                if (i  < 9) // 1 ~ 9 호
                    buttons[i].setText(floor + "0" + (i+1));
                else        // 10 ~ 호
                    buttons[i].setText(floor + (i+1));
                layout.addView(buttons[i]);
            }
        }
    }
}
