package com.stafor.gachonclass;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ClassLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

    CheckBox checkBox;
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

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        imageView = (ImageView) findViewById(R.id.campusImage);
        classRoomTextView = (TextView) findViewById(R.id.classRoom);
        timeTextView = (TextView) findViewById(R.id.time);
    }

    public CheckBox getCheckBox() { return checkBox; }
    public void setImage(int resId) {imageView.setImageResource(resId); }
    public void setNameText(String name) {
        classRoomTextView.setText(name);
    }
    public void setTimeText(String company) {
        timeTextView.setText(company);
    }
}
