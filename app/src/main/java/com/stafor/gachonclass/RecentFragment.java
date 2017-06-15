package com.stafor.gachonclass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentFragment extends Fragment implements View.OnClickListener{
    LinearLayout layout, layout_bottom;
    String time, floor, building, classRoom, id;
    Button modifyBtn, allBtn, removeBtn, cancelBtn;

    boolean onModify;   // 편집모드
    AlertDialog.Builder builder;
    final String[] items = {"시간표 조회", "알림설정", "예약문의", "즐겨찾기 등록"};

    DBHelper_Recent dbHelper_recent;
    DBHelper_Bookmark dbHelper_bookmark;

    MyAdapter adapter;
    ArrayList<ClassItem> album = new ArrayList<ClassItem>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recent, container, false);

        // 리스트 뷰를 찾아와서 어뎁터를 연결한다
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new MyAdapter(getContext(), R.layout.class_item, album);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 아이템을 클릭 시 다이어로그를 띄움
                building = album.get(position).building;
                floor = album.get(position).floor;
                classRoom = album.get(position).classRoom;
                builder.setTitle(building + " " + floor + "F " + classRoom + "호");
                builder.show();
            }
        });

        onModify = false;   // 편집모드 off
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

                if (building.equals("IT대학")) {
                    album.add(new ClassItem(R.drawable.computer, building, floor, classRoom,
                            time, Integer.parseInt(id)));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_modify) { // 수정버튼
                layout_bottom.setVisibility(View.VISIBLE);
                modifyBtn.setVisibility(View.GONE);
                for (int i = 0; i < album.size(); i++) {
                    album.get(i).checkBox.setVisibility(View.VISIBLE);
                }
                onModify = true;
        } else if (v.getId() == R.id.btn_all) {
            for (int i = 0; i < album.size(); i++) {
                album.get(i).checkBox.setChecked(true);
            }
        }else if (v.getId() == R.id.btn_ok) {  // 삭제버튼
            removeRecent();
        } else if (v.getId() == R.id.btn_cancel) {  // 취소버튼
            layout_bottom.setVisibility(View.GONE);
            modifyBtn.setVisibility(View.VISIBLE);
            for (int i = 0; i < album.size(); i++) {
                album.get(i).checkBox.setVisibility(View.GONE);
            }
            onModify = false;
        }
    }

    public void removeRecent() {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i).checkBox.isChecked()) {
                dbHelper_recent.delete(album.get(i).id);
                album.remove(i--);   // 리스트에서 삭제 후 이전 i로 돌아간다
                Log.e("size", "size = " + album.size());
            }
        }
    }

    class ClassItem {
        CheckBox checkBox;
        int resId;//이미지 리소스 id
        String building, floor, classRoom, time;
        int id;

        //생성자
        public ClassItem(int resId, String building, String floor, String classRoom,
                         String time, int id) {
            this.resId = resId;
            this.building = building;
            this.floor = floor;
            this.classRoom = classRoom;
            this.time = time;
            this.id = id;
        }

        public int getResId() {return resId;}
        public void setResId(int resId) {this.resId = resId;}
        public String getclassRoom() {return classRoom;}
        public void setclassRoom(String classRoom) {this.classRoom = classRoom;}
        public String getTime() {return time;}
        public void setTime(String time) {this.time = time;}
    }

    class MyAdapter extends BaseAdapter {
        Context mContext;
        int class_item;
        ArrayList<ClassItem> album;
        LayoutInflater inflater;

        public MyAdapter(Context context, int class_item, ArrayList<ClassItem> album) {
            mContext = context;
            this.class_item = class_item;
            this.album = album;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return album.size();
        }
        @Override
        public Object getItem(int position) {
            return album.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        public void addItem(ClassItem item) {
            album.add(item);
        }
        public void clear() {
            album.clear();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassLayout classLayout = null;

            if (convertView == null) {
                classLayout = new ClassLayout(mContext);
            } else {
                classLayout = (ClassLayout) convertView;
            }

            ClassItem items = album.get(position);
            items.checkBox = classLayout.getCheckBox();
            classLayout.setImage(items.getResId());
            classLayout.setNameText(items.getclassRoom());
            classLayout.setTimeText(items.getTime());

            return classLayout;
        }
    }
}
