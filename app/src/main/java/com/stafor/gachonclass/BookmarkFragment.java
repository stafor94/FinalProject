package com.stafor.gachonclass;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment implements View.OnClickListener{
    int item_position;
    String time, floor, building, classRoom, id;
    Button resetBtn;
    AlertDialog.Builder builder;
    final String[] items = {"시간표 조회", "알림설정", "예약문의", "삭제"};

    DBHelper_Bookmark dbHelper_bookmark;

    MyAdapter adapter;
    ArrayList<ClassItem> album = new ArrayList<ClassItem>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_bookmark, container, false);

        // 리스트 뷰를 찾아와서 어뎁터를 연결한다
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new MyAdapter(getContext(), R.layout.class_item, album);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 아이템을 클릭 시 다이어로그를 띄움
                item_position = position;
                building = album.get(position).building;
                floor = album.get(position).floor;
                classRoom = album.get(position).classRoom;
                builder.setTitle(building + " " + floor + "F " + classRoom + "호");
                builder.show();
            }
        });

        dbHelper_bookmark = new DBHelper_Bookmark(getContext());
        resetBtn = (Button) rootView.findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(this);

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
                        dbHelper_bookmark.delete(album.get(item_position).id);
                        album.remove(item_position);
                        adapter.notifyDataSetInvalidated();
                        break;
                }
            }
        });
        builder.create();

        return rootView;
    }

    public void initData() {
        int count = dbHelper_bookmark.checkTableCount(); // table 내의 행 개수를 받아온다

        try {
            album.clear();  // album을 초기화 한다
            for (int i = 0; i < count; i++) {
                id = dbHelper_bookmark.printData(i, 0);
                building = dbHelper_bookmark.printData(i, 1);
                floor = dbHelper_bookmark.printData(i, 2);
                classRoom = dbHelper_bookmark.printData(i, 3);

                // iT대학의 최근 기록을 album에 추가
                if (building.equals("IT대학"))
                    album.add(new ClassItem(R.drawable.computer, building, floor, classRoom,
                            null, Integer.parseInt(id)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset) { // 초기화버튼
            album.clear();
            dbHelper_bookmark.clear();
            adapter.notifyDataSetInvalidated();
        }
    }

    class ClassItem {
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
        public String getClassRoom() {return classRoom;}
        public void setClassRoom(String classRoom) {this.classRoom = classRoom;}
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
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassLayout classLayout = null;

            if (convertView == null)
                classLayout = new ClassLayout(mContext);
            else
                classLayout = (ClassLayout) convertView;

            ClassItem items = album.get(position);
            classLayout.setImage(items.getResId());
            classLayout.setNameText(items.getClassRoom());
            classLayout.setTimeText("");

            return classLayout;
        }
    }
}
