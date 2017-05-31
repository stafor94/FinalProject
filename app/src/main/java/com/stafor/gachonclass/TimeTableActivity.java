package com.stafor.gachonclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static com.stafor.gachonclass.R.id.layout_mon;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {
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
    int colorIndex = 0;
    int[] colors = {R.color.colorLightGreen, R.color.colorLightBlue, R.color.colorPink,
                    R.color.colorYellow, R.color.colorPurple, R.color.colorDarkGreen, R.color.colorDarkBlue};
    ArrayList<TimeList> list = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog dialog;
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
        String start, end;

        Intent myIntent = getIntent();
        building = myIntent.getStringExtra("building");
        classRoom = myIntent.getStringExtra("classroom");
        Log.e("dd", "[" + building + "][" + classRoom + "]");

        makeClass(index);

        tableSize = dbHelper.checkClassRoom(days[index], classRoom);
        for (int i = 0; i < tableSize; i++) {
            start = dbHelper.printData(days[index], classRoom, i, START);
            end = dbHelper.printData(days[index], classRoom, i, END);

            for (int j = (int) convert(start); j <= (int) convert(end); j++) {
                String subject, professor, major;
                subject = dbHelper.printData(days[index], classRoom, i, SUBJECT);
                professor = dbHelper.printData(days[index], classRoom, i, PROFESSOR);
                major = dbHelper.printData(days[index], classRoom, i, MAJOR);

                Button btn = (Button) layout[index].findViewWithTag("btn_" + days[index] + "_" + Integer.toString(j));
                btn.setBackgroundColor(getResources().getColor(colors[colorIndex]));
                btn.setOnClickListener(this);

                if (j == (int) convert(start))
                    btn.setText(subject);
                if (j == (int) convert(end)) {
                    if ((int) convert(start) == (int) convert(end))
                        btn.setText(subject + "\n" + professor);
                    else
                        btn.setText(professor);
                }

                list.add(new TimeList(btn, subject, professor, major));
            }
            colorIndex++;
            if (colorIndex > colors.length - 1)
                colorIndex = 0;
        }
    }

    public void makeClass(int index) {
        for (int i = 1; i <= 15; i++) {
            Button btn = new Button(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    150, 0);
            btn.setTag("btn_" + days[index] + "_" + i);
            btn.setTextSize(9);
            btn.setGravity(Gravity.CENTER_HORIZONTAL);
            btn.setLayoutParams(param);
            btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            layout[index].addView(btn);
        }
    }

    public float convert(String time) {
        float result;

        if (time.equals("A"))
            result = 1.5f;
        else if (time.equals("B")) {
            result = 3.0f;
        } else if (time.equals("C")) {
            result = 4.5f;
        } else if (time.equals("D")) {
            result = 6.0f;
        } else if (time.equals("E")) {
            result = 7.5f;
        }
        else
            result = Float.parseFloat(time);

        return result;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).btn == (Button)v) {
                builder = new AlertDialog.Builder(this);
                builder.setTitle("수업 정보")
                        .setMessage("과목: " + list.get(i).subject + "\n교수: " +
                                list.get(i).professor + "\n학과: " + list.get(i).major)
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        }
    }

    class TimeList {
        Button btn;
        String subject, professor, major;

        public TimeList(Button btn, String subject, String professor, String major) {
            this.btn = btn;
            this.subject = subject;
            this.professor = professor;
            this.major = major;
        }
    }

}
