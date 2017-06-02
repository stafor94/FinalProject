package com.stafor.gachonclass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AlarmManagerActivity";
    private static final String INTENT_ACTION = "com.stafor.gachonclass";

    Button okBtn, cancelBtn;
    Calendar mCalendar = Calendar.getInstance();
    TimePicker timePicker;
    DatePicker datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        init();
    }

    public void init() {
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
            }
        });
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                    }
                });

        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                getTime();
                new AlarmHATT(getApplicationContext()).Alarm();
                Toast.makeText(this, "알림이 설정되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_cancel:
                finish();
        }
    }

    public void getTime() {

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

            //알림시간 calendar에 set해주기
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DATE), mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND));

            //알림 예약
            am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), sender);
        }
    }

}
