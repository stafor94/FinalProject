package com.stafor.gachonclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.stafor.gachonclass.R.id.layout_mon;

public class TimeTableActivity extends AppCompatActivity {
    LinearLayout[] layout = new LinearLayout[5];
    final int CLASS = 1;
    final int DAY = 2;
    final int START = 3;
    final int END = 4;
    final int SUBJECT = 5;
    final int PROFESSOR = 6;
    final int MAJOR = 7;
    String building, classRoom;
    String[] days = { "mon", "tue", "wed", "thu", "fri" };
    int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY};

    DBHelper_TimeTable dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        dbHelper = new DBHelper_TimeTable(this);

        init();
    }

    public void init() {
        layout[0] = (LinearLayout) findViewById(layout_mon);
        layout[1] = (LinearLayout) findViewById(R.id.layout_tue);
        layout[2] = (LinearLayout) findViewById(R.id.layout_wed);
        layout[3] = (LinearLayout) findViewById(R.id.layout_thu);
        layout[4] = (LinearLayout) findViewById(R.id.layout_fri);

        for (int i = 0; i < 5; i++) // 월 ~ 금 테이블을 채운다.
            fillTable(i);
    }

    public void fillTable(int index) {
        int tableSize;
        float padding;
        String start, end;

        Intent myIntent = getIntent();
        building = myIntent.getStringExtra("building");
        classRoom = myIntent.getStringExtra("classroom");
        Log.e("dd", "[" + building + "][" + classRoom + "]");

        tableSize = dbHelper.checkClassRoom(days[index], classRoom);
        for (int i = 0; i < tableSize; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(dbHelper.printData(days[index], classRoom, i, SUBJECT));
            tv.setBackgroundColor(colors[i]);
            start = dbHelper.printData(days[index], classRoom, i, START);
            end = dbHelper.printData(days[index], classRoom, i, END);

            padding = convert(end) - convert(start);    // 시간의 길이를 구한다
            tv.setPadding(0, (int) (padding * 50.0f), 0, (int) (padding * 150.0f));  // 상,하 padding 설정

            layout[index].addView(tv);  // 요일 레이아웃에 부착
        }
    }

    public float convert(String time) {
        float result;
        if (time.equals("A"))
            result = 1.5f;
        else if (time.equals("B"))
            result = 3.0f;
        else if (time.equals("C"))
            result = 4.5f;
        else if (time.equals("D"))
            result = 6.0f;
        else if (time.equals("E"))
            result = 7.5f;
        else
            result = Float.parseFloat(time);

        return result;
    }
}
