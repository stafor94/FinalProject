package com.stafor.gachonclass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    private static final String TAG = "AlarmManagerActivity";
    private static final String INTENT_ACTION = "com.stafor.gachonclass";

    Button okBtn, cancelBtn;
    RadioGroup rgTime, rgDetail;
    RadioButton rbBefore, rbAfter, rbBefore2, rbAfter2;
    Calendar mCalendar = Calendar.getInstance();

    int start, end;
    boolean boolTime, boolDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        init();
    }

    public void init() {
        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        rgTime = (RadioGroup) findViewById(R.id.rg_1);
        rgDetail = (RadioGroup) findViewById(R.id.rg_2);
        rbBefore = (RadioButton) findViewById(R.id.rb_1);
        rbAfter = (RadioButton) findViewById(R.id.rb_2);
        rbBefore2 = (RadioButton) findViewById(R.id.rb_3);
        rbAfter2 = (RadioButton) findViewById(R.id.rb_4);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        rgTime.setOnCheckedChangeListener(this);
        rgDetail.setOnCheckedChangeListener(this);

        Intent myIntent = getIntent();
        start = myIntent.getIntExtra("start", 0);
        end = myIntent.getIntExtra("end", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                new AlarmHATT(getApplicationContext()).Alarm();
                Toast.makeText(this, "알림이 설정되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_cancel:
                finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_1:
                boolTime = true;
                break;
            case R.id.rb_2:
                boolTime = false;
                break;
            case R.id.rb_3:
                boolDetail = true;
                break;
            case R.id.rb_4:
                boolDetail = false;
                break;
        }
    }

    public void getTime() {
        if (boolTime && boolDetail) {   // 수업 시작 5분 전
            mCalendar.set(Calendar.HOUR_OF_DAY, start - 1);
            mCalendar.set(Calendar.MINUTE, 55);
            mCalendar.set(Calendar.SECOND, 0);
        }
        else if (!boolTime && boolDetail) { // 수업 종료 5분 전
            mCalendar.set(Calendar.HOUR_OF_DAY, end - 1);
            mCalendar.set(Calendar.MINUTE, 55);
            mCalendar.set(Calendar.SECOND, 0);
        }
        else if (boolTime && !boolDetail) { // 수업 시작 5분 후
            mCalendar.set(Calendar.HOUR_OF_DAY, start);
            mCalendar.set(Calendar.MINUTE, 5);
            mCalendar.set(Calendar.SECOND, 0);
        }
        else {  // 수업 종료 5분 후
            mCalendar.set(Calendar.HOUR_OF_DAY, end);
            mCalendar.set(Calendar.MINUTE, 5);
            mCalendar.set(Calendar.SECOND, 0);
        }
    }

    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmActivity.this, AlarmBroadcast.class);

            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 3, 23, 0);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            Log.e("am", "알람 예약");
        }
    }

}
