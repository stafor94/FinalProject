package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentFragment extends Fragment {
    LinearLayout layout;
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String time;
    ArrayList<Button> btnList = new ArrayList<>();
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recent, container, false);

        layout = (LinearLayout) rootView.findViewById(R.id.layout_btns);

        addButton("가천관", "3", "302");
        addButton("IT대학", "5", "502");

        return rootView;
    }

    public void addButton(String building, String floor, String classRoom) {
        time = sdfNow.format(new Date(System.currentTimeMillis()));
        Button button = new Button(getContext());
        button.setText(building + " " + classRoom + "호 |" + time);
        btnList.add(button);
        layout.addView(btnList.get(count++));
    }
}
