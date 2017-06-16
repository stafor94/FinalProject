package com.stafor.gachonclass;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ClassLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

    ImageView imageView;
    TextView classRoomTextView;
    TextView timeTextView;

    public ClassLayout(Context context) {
        super(context);
        mContext = context;

        init();
    }

    public ClassLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        init();
    }

    private void init() {
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.class_item, this, true);

        imageView = (ImageView) findViewById(R.id.campusImage);
        classRoomTextView = (TextView) findViewById(R.id.classRoom);
        timeTextView = (TextView) findViewById(R.id.time);
    }

    public void setImage(int resId) {imageView.setImageResource(resId); }
    public void setNameText(String name) {classRoomTextView.setText(name + "호");}
    public void setTimeText(String company) {
        timeTextView.setText(company);
    }
}
