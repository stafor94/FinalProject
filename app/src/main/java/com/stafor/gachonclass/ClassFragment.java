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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ClassFragment extends Fragment {
    GridLayout layout;
    int floor;
    String building, classRoom, time, cur_week, cur_hour, cur_min;
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    AlertDialog.Builder builder;
    ArrayList<SeekList> list = new ArrayList<>();

    DBHelper_Recent dbHelper_recent;
    DBHelper_ClassRoom dbHelper_classRoom;
    DBHelper_TimeTable dbHelper_timeTable;
    DBHelper_Bookmark dbHelper_bookmark;

    TextView weekTv, hourTv, minTv;

    public static final String gachonTel = "031-750-5151";  //가천관 번호
    public static final String visionTel = "031-750-5551";  //비전타워 번호
    public static final String lawTel = "031-750-8621";  //법과대학 번호
    public static final String engineeringTel = "031-750-5271"; //공과대학
    public static final String nanoTel = "031-750-5381"; //바이오나노
    public static final String itTel = "031-750-5661";
    public static final String medicineTel = "031-750-5401"; //한의학
    public static final String artTel = "031-750-5851"; //예술대
    public static final String englishTel = "031-750-5114";  //학교번호
    final String[] items = {"시간표 조회", "알림설정", "예약문의", "즐겨찾기 등록"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_class, container, false);
        layout = (GridLayout) rootView.findViewById(R.id.layout);
        dbHelper_recent = new DBHelper_Recent(getContext());
        dbHelper_bookmark = new DBHelper_Bookmark(getContext());
        dbHelper_classRoom = new DBHelper_ClassRoom(getContext());
        dbHelper_timeTable = new DBHelper_TimeTable(getContext());

        weekTv = (TextView) rootView.findViewById(R.id.tv_week);
        hourTv = (TextView) rootView.findViewById(R.id.tv_hour);
        minTv = (TextView) rootView.findViewById(R.id.tv_min);

        init();

        // 다이어로그 생성 밑 설정
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(building + " " + floor);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), items[which] + " 선택!", Toast.LENGTH_SHORT).show();
                if (which == 0 && building.equals("IT대학")) {
                    Intent myIntent = new Intent(getContext(), TimeTableActivity.class);
                    myIntent.putExtra("classroom", classRoom);
                    myIntent.putExtra("building", building);
                    startActivity(myIntent);
                }
                if (which == 1 && building.equals("IT대학")) {
                    Intent myIntent = new Intent(getContext(), AlarmActivity.class);
                    myIntent.putExtra("start", 0);
                    myIntent.putExtra("end", 0);
                    startActivity(myIntent);
                }
                if (which == 2 && building.equals("IT대학")){
                    Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" +itTel));
                    startActivity(callIntent);
                }
                if (which == 3 && building.equals("IT대학")) {
                    dbHelper_bookmark.insert(building, Integer.toString(floor), classRoom);
                }
            }
        });
        builder.create();
        return rootView;
    }

    public void init() {
        Date now = new Date();
        cur_week = new SimpleDateFormat("EEE", Locale.UK).format(now);
        cur_hour = new SimpleDateFormat("HH").format(now);
        cur_min = new SimpleDateFormat("mm").format(now);
        weekTv.setText(cur_week);
        hourTv.setText(cur_hour);
        minTv.setText(cur_min);

        Bundle bundle = getArguments();
        if (bundle != null) {
            // 건물 이름과 층수를 받아온다.
            building = bundle.getString("building");
            floor = bundle.getInt("floor");
            createClassRoom();
        }
    }

    public void createClassRoom() {
        String mClassRoom;
        time = sdfNow.format(new Date(System.currentTimeMillis()));
        for(int i = 0; i < dbHelper_classRoom.checkFloor(floor); i++) {
            mClassRoom = dbHelper_classRoom.printData(i, floor);   // 강의실 번호 받아오기

            Button btn = new Button(getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 0);
            param.setMargins(50, 50, 50, 50);
            btn.setText(mClassRoom);
            btn.setLayoutParams(param);

            if (dbHelper_timeTable.isInClass(cur_week, cur_hour, mClassRoom))
                btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            else
                btn.setBackgroundColor(getResources().getColor(R.color.colorLightGreen));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classRoom = ((Button)v).getText().toString();
                    builder.setTitle(building + " "+ floor + "F " + classRoom + "호");
                    builder.show();
                    time = sdfNow.format(new Date(System.currentTimeMillis()));
                    dbHelper_recent.insert(building, Integer.toString(floor), classRoom, time);
                }
            });
            list.add(new SeekList(btn, building, floor, classRoom));
            layout.addView(btn);
        }
    }

    class SeekList {
        Button button;
        int floor;
        String building, classRoom;

        public SeekList(Button button, String building, int floor, String classRoom) {
            this.button = button;
            this.building = building;
            this.floor = floor;
            this.classRoom = classRoom;
        }
    }
}
