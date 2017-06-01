package com.stafor.gachonclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static com.stafor.gachonclass.R.id.layout_mon;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout[] layout = new LinearLayout[5];
    final int CLASS = 1, DAY = 2, START = 3, END = 4, SUBJECT = 5, PROFESSOR = 6, MAJOR = 7;

    String building, classRoom;
    String[] days = { "mon", "tue", "wed", "thu", "fri" };
    int colorIndex = 0;
    int[] colors = {R.color.colorLightGreen, R.color.colorLightBlue, R.color.colorPink,
                    R.color.colorYellow, R.color.colorPurple, R.color.colorDarkGreen, R.color.colorDarkBlue};
    boolean isFirst = false;

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

        isFirst = false;
        makeClass(index);
        tableSize = dbHelper.checkClassRoom(days[index], classRoom);
        for (int i = 0; i < tableSize; i++) {
            start = dbHelper.printData(days[index], classRoom, i, START);
            end = dbHelper.printData(days[index], classRoom, i, END);

            for (int j = convert(start); j <= convert(end); j++) {
                String subject, professor, major;
                subject = dbHelper.printData(days[index], classRoom, i, SUBJECT);
                professor = dbHelper.printData(days[index], classRoom, i, PROFESSOR);
                major = dbHelper.printData(days[index], classRoom, i, MAJOR);

                Button preBtn = null, btn, nextBtn = null;
                if (j != 0)
                    preBtn = (Button) layout[index].findViewWithTag("btn_" + days[index] + "_" + Integer.toString(j-1));
                btn = (Button) layout[index].findViewWithTag("btn_" + days[index] + "_" + Integer.toString(j));
                if (j != 13)
                    nextBtn = (Button) layout[index].findViewWithTag("btn_" + days[index] + "_" + Integer.toString(j+1));

                btn.setBackgroundColor(getResources().getColor(colors[colorIndex]));
                btn.setOnClickListener(this);
                if (j == convert(start))
                    btn.setText(subject + "\n" + professor);

                if (!isNum(start) && j == convert(start)) {
                    changeParam(preBtn, btn, nextBtn, start);
                } else if (!isNum(end) && j == convert(end)) {
                    changeParam(preBtn, btn, nextBtn, end);
                }

                list.add(new TimeList(btn, subject, professor, major, start, end));
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

    public int convert(String time) {
        int result;

        if (time.equals("A"))
            result = 1;
        else if (time.equals("B")) {
            result = 3;
        } else if (time.equals("C")) {
            result = 4;
        } else if (time.equals("D")) {
            result = 6;
        } else if (time.equals("E")) {
            result = 7;
        }
        else
            result = Integer.parseInt(time);

        return result;
    }

    public boolean isNum(String time) {
        if (time.equals("A") || time.equals("B") || time.equals("C") || time.equals("D")
                || time.equals("E"))
            return false;
        else
            return true;
    }

    public void changeParam(Button preBtn, Button btn, Button nextBtn, String time) {

        if (time.equals("A")) {
            isFirst = true;
            btn.setLayoutParams(getMargines(150 + 75, 75));
            nextBtn.setLayoutParams(getMargines(150 - 75, 0));
        } else if (time.equals("B")) {
            if (!list.contains(preBtn)) {
                preBtn.setLayoutParams(getMargines(0, 0));
            }
            if (!isFirst)
                btn.setLayoutParams(getMargines(150 + 75, 150));
            else
                btn.setLayoutParams(getMargines(150 + 75, 0));
            nextBtn.setLayoutParams(getMargines(150 - 75, 0));
        } else if (time.equals("C")) {
            btn.setLayoutParams(getMargines(150 + 75, 0));
            for (int i = 0; i < list.size(); i++)
                if (list.get(i).btn == preBtn && !list.get(i).end.equals("B") && !list.get(i).start.equals("B"))
                    btn.setLayoutParams(getMargines(150 + 75, 75));
            nextBtn.setLayoutParams(getMargines(0, 0));
        } else if (time.equals("D")) {
            btn.setLayoutParams(getMargines(150 + 75, 0));
            nextBtn.setLayoutParams(getMargines(150 - 75, 0));
        } else if (time.equals("E")) {
            btn.setLayoutParams(getMargines(150 + 75, 0));
            for (int i = 0; i < list.size(); i++)
                if (list.get(i).btn == preBtn && list.get(i).end.equals("6"))
                    btn.setLayoutParams(getMargines(150 + 75, 75));
        }
    }

    public LinearLayout.LayoutParams getMargines(int heigth, int top) {
        LinearLayout.LayoutParams param;
        param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                heigth, 0);
        param.setMargins(0, top, 0, 0);

        return param;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).btn == (Button)v) {
                builder = new AlertDialog.Builder(this);
                builder.setTitle("수업 정보")
                        .setMessage("과목: " + list.get(i).subject + "\n교수: " +
                                list.get(i).professor + "\n학과: " + list.get(i).major
                                + "\n강의 시간 : " + list.get(i).start + "교시 - " + list.get(i).end
                                + "교시")
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
        String subject, professor, major, start, end;

        public TimeList(Button btn, String subject, String professor, String major, String start, String end) {
            this.btn = btn;
            this.subject = subject;
            this.professor = professor;
            this.major = major;
            this.start = start;
            this.end = end;
        }
    }

}
