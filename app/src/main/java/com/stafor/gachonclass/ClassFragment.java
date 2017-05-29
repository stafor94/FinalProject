package com.stafor.gachonclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassFragment extends Fragment {
    GridLayout layout;
    String building, floor, classRoom;
    Button[] buttons;
    AlertDialog.Builder builder;
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final String gachonTel = "031-750-5151";  //가천관 번호
    public static final String visionTel = "031-750-5551";  //비전타워 번호
    public static final String lawTel = "031-750-8621";  //법과대학 번호
    public static final String engineeringTel = "031-750-5271"; //공과대학
    public static final String nanoTel = "031-750-5381"; //바이오나노
    public static final String itTel = "031-750-5661";
    public static final String medicineTel = "031-750-5401"; //한의학
    public static final String artTel = "031-750-5851"; //예술대
    public static final String englishTel = "031-750-5114";  //학교번호
    String time;

    DBHelper_Recent dbHelper;

    final String[] items = {"수업정보 조회", "시간표 조회", "알림설정", "예약문의"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_class, container, false);
        layout = (GridLayout) rootView.findViewById(R.id.layout);
        dbHelper = new DBHelper_Recent(getContext());
        init();

        // 다이어로그 생성 밑 설정
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(building + " " + floor);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), items[which] + " 선택!", Toast.LENGTH_SHORT).show();
                if(which ==3 && building.equals("IT대학")){
                    Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" +itTel));
                    startActivity(callIntent);
                }
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
                        time = sdfNow.format(new Date(System.currentTimeMillis()));
                        dbHelper.insert(building, floor, classRoom, time);
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
