package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class CampusFragment extends Fragment implements View.OnClickListener{
    Button buttons[] = new Button[11];
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_campus, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.img_campus);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), ImageActivity.class);
                myIntent.putExtra("img", R.drawable.campusmap);
                startActivity(myIntent);
            }
        });

        // 태그를 이용하여 버튼을 찾고 리스너 연결
        for (int i = 0; i < 11; i++) {
            buttons[i] = (Button) rootView.findViewWithTag("build" + i);
            buttons[i].setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        String building = ((Button)v).getText().toString();
        Intent myIntent = new Intent(getContext(), SeekActivity.class);
        myIntent.putExtra("building", building);
        startActivity(myIntent);
    }
}
