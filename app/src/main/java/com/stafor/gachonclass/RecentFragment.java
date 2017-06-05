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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentFragment extends Fragment implements View.OnClickListener{
    LinearLayout layout, layout_bottom;
    String time, floor, building, classRoom, id;
    ArrayList<RecentList> list = new ArrayList<>();
    Button modifyBtn, allBtn, removeBtn, cancelBtn;
    boolean onModify;
    AlertDialog.Builder builder;
    final String[] items = {"시간표 조회", "알림설정", "예약문의", "즐겨찾기 등록"};

    DBHelper_Recent dbHelper_recent;
    DBHelper_Bookmark dbHelper_bookmark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recent, container, false);

        onModify = false;
        dbHelper_recent = new DBHelper_Recent(getContext());
        dbHelper_bookmark = new DBHelper_Bookmark(getContext());
        layout = (LinearLayout) rootView.findViewById(R.id.layout_btns);
        layout_bottom = (LinearLayout) rootView.findViewById(R.id.layout_bottom);
        modifyBtn = (Button) rootView.findViewById(R.id.btn_modify);
        allBtn = (Button) rootView.findViewById(R.id.btn_all);
        removeBtn = (Button) rootView.findViewById(R.id.btn_ok);
        cancelBtn = (Button) rootView.findViewById(R.id.btn_cancel);
        modifyBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        initData(); // SQLite에서 데이터 가져와서 출력

        // 다이어로그 생성 밑 설정
        builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (building.equals("IT대학")) {
                            Intent myIntent = new Intent(getContext(), TimeTableActivity.class);
                            myIntent.putExtra("classroom", classRoom);
                            myIntent.putExtra("building", building);
                            startActivity(myIntent);
                        }
                        break;
                    case 1:
                        Intent myIntent = new Intent(getContext(), AlarmActivity.class);
                        startActivity(myIntent);
                        break;
                    case 2:
                        if (building.equals("IT대학")) {
                            Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + ClassFragment.itTel));
                            startActivity(callIntent);
                        }
                        break;
                    case 3:
                        dbHelper_bookmark.insert(building, floor, classRoom);
                        Toast.makeText(getContext(), "즐겨찾기에 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.create();

        return rootView;
    }

    public void initData() {
        int count = dbHelper_recent.checkTableCount(); // table 내의 행 개수를 받아온다
        try {
            for (int i = 0; i < count; i++) {
                id = dbHelper_recent.printData(i, 0);
                building = dbHelper_recent.printData(i, 1);
                floor = dbHelper_recent.printData(i, 2);
                classRoom = dbHelper_recent.printData(i, 3);
                time = dbHelper_recent.printData(i, 4);

                if (!building.equals(""))
                    addButton(building, floor, classRoom, time, id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addButton(String building, String floor, String classRoom, String time, String id) {
        Button button = new Button(getContext()); // 버튼 생성
        CheckBox chk_box = new CheckBox(getContext());  // 체크박스 생성
        LinearLayout layout_line = new LinearLayout(getContext());   // 버튼과 체크박스를 담을 레이아웃

        button.setText("  " + building + " " + classRoom + "호  |  " + time);    // 버튼 내용

        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(param);  // 버튼의 레이아웃 설정
        button.setOnClickListener(this);

        list.add(new RecentList(layout_line, button, chk_box, building, floor, classRoom, time, Integer.parseInt(id)));  // 어레이리스트에 버튼과 체크박스 추가
        button.setId(Integer.parseInt(id));
        chk_box.setVisibility(View.GONE);  // 체크박스 숨기기
        // 레이아웃에 위젯 부착
        layout_line.addView(chk_box);
        layout_line.addView(button);
        layout.addView(layout_line);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_modify) { // 수정버튼
                layout_bottom.setVisibility(View.VISIBLE);
                modifyBtn.setVisibility(View.GONE);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).chk_box.setVisibility(View.VISIBLE);
                    list.get(i).button.setClickable(false);
                }
                onModify = true;
        } else if (v.getId() == R.id.btn_all) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).chk_box.setChecked(true);
            }
        }else if (v.getId() == R.id.btn_ok) {  // 삭제버튼
            removeRecent();
        } else if (v.getId() == R.id.btn_cancel) {  // 취소버튼
            layout_bottom.setVisibility(View.GONE);
            modifyBtn.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).chk_box.setVisibility(View.GONE);
                list.get(i).button.setClickable(true);
            }
            onModify = false;
        } else if (!onModify){  // 편집 중이 아니면
            for (int i = 0; i < list.size(); i++) {  // 모든 리스트를 비교하며
                if (v.getId() == list.get(i).id) {  // 누른 버튼과 어레이리스트의 id를 비교
                    building = list.get(i).building;
                    floor = list.get(i).floor;
                    classRoom = list.get(i).classRoom;
                    builder.setTitle(building + " " + floor + "F " + classRoom + "호");
                    builder.show();
                    break;
                }
            }
        }
    }

    public void removeRecent() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).chk_box.isChecked()) {
                dbHelper_recent.delete(list.get(i).id);
                list.get(i).layout.setVisibility(View.GONE);
                list.remove(i--);   // 리스트에서 삭제 후 이전 i로 돌아간다
            }
        }
    }

    class RecentList {
        LinearLayout layout;
        Button button;
        CheckBox chk_box;
        String building, floor, classRoom, time;
        int id;

        public RecentList(LinearLayout layout, Button button, CheckBox chk_box, String building, String floor, String classRoom, String time, int id) {
            this.id = id;
            this.layout = layout;
            this.button = button;
            this.chk_box = chk_box;
            this.building = building;
            this.floor = floor;
            this.classRoom = classRoom;
            this.time = time;
        }
    }
}
