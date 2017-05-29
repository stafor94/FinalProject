package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentFragment extends Fragment implements View.OnClickListener{
    LinearLayout layout, layout_bottom;
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String time, building, classRoom;
    ArrayList<Button> btnList = new ArrayList<>();
    ArrayList<CheckBox> chkList = new ArrayList<>();
    Button modifyBtn, removeBtn, cancelBtn;
    boolean onModify = false;
    int count = 0;

    DBHelper_Recent dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recent, container, false);

        dbHelper = new DBHelper_Recent(getContext());
        layout = (LinearLayout) rootView.findViewById(R.id.layout_btns);
        layout_bottom = (LinearLayout) rootView.findViewById(R.id.layout_bottom);
        modifyBtn = (Button) rootView.findViewById(R.id.btn_modify);
        removeBtn = (Button) rootView.findViewById(R.id.btn_ok);
        cancelBtn = (Button) rootView.findViewById(R.id.btn_cancel);
        modifyBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        try {
            for (int i = 0; i < 5; i++) {
                building = dbHelper.printData(i, 1);
                classRoom = dbHelper.printData(i, 2);
                if (!building.equals(""))
                    addButton(building, classRoom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public void addButton(String building, String classRoom) {
        time = sdfNow.format(new Date(System.currentTimeMillis()));
        Button button = new Button(getContext());
        LinearLayout layout_btn = new LinearLayout(getContext());
        CheckBox chk_box = new CheckBox(getContext());
        button.setText("  " + building + " " + classRoom + "호  |  " + time);
        btnList.add(button);
        chkList.add(chk_box);
        chk_box.setVisibility(View.GONE);
        layout_btn.addView(chk_box);
        layout_btn.addView(btnList.get(count++));
        layout.addView(layout_btn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_modify) {
            if (onModify) {
                layout_bottom.setVisibility(View.GONE);
                for (int i = 0; i < chkList.size(); i++)
                    chkList.get(i).setVisibility(View.GONE);
            } else {
                layout_bottom.setVisibility(View.VISIBLE);
                for (int i = 0; i < chkList.size(); i++)
                    chkList.get(i).setVisibility(View.VISIBLE);
            }

            onModify = !onModify;
        } else if (v.getId() == R.id.btn_ok) {
        } else if (v.getId() == R.id.btn_cancel) {
            layout_bottom.setVisibility(View.GONE);
        }
    }
}
