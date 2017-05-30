package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeTableActivity extends AppCompatActivity {
    LinearLayout layout_mon, layout_tue, layout_wed, layout_thu, layout_fri;
    final int CLASS = 1;
    final int DAY = 2;
    final int START = 3;
    final int END = 4;
    final int SUBJECT = 5;
    final int PROFESSOR = 6;
    final int MAJOR = 7;
    String building, classRoom;

    DBHelper_TimeTable dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        dbHelper = new DBHelper_TimeTable(this);

        init();
    }

    public void init() {
        layout_mon = (LinearLayout) findViewById(R.id.layout_mon);
        layout_tue = (LinearLayout) findViewById(R.id.layout_tue);
        layout_wed = (LinearLayout) findViewById(R.id.layout_wed);
        layout_thu = (LinearLayout) findViewById(R.id.layout_thu);
        layout_fri = (LinearLayout) findViewById(R.id.layout_fri);

        fillTable("월");
        fillTable("화");
        fillTable("수");
        fillTable("목");
        fillTable("금");
    }

    public void fillTable(String day) {
        int tableSize;
        float padding;
        String start, end;

        Intent myIntent = getIntent();
        building = myIntent.getStringExtra("building");
        classRoom = myIntent.getStringExtra("classroom");

        tableSize = dbHelper.checkClassRoom(day, classRoom);
        for (int i = 0; i < tableSize; i++) {
            TextView tv = new TextView(this);
            tv.setText(dbHelper.printData(day, classRoom, i, SUBJECT));
            start = dbHelper.printData(day, classRoom, i, START);
            end = dbHelper.printData(day, classRoom, i, END);
            if (start.equals("") || end.equals("")) {
                Log.e("dbError", "start or end = \"\"");
            }
            padding = convert(end) - convert(start);
            tv.setPadding(0, (int) (padding * 20.0f), 0, (int) (padding * 20.0f));
            layout_mon.addView(tv);
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
