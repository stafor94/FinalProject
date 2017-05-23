package com.stafor.gachonclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CampusFragment extends Fragment {
    LinearLayout layout;
    ImageView imageView;
    Button[] buttons = new Button[11];
    String[] names = {"가천관", "비전타워", "법과대학", "공과대학1", "공과대학2", "바이오나노대학",
            "IT대학", "한의과대학", "예술대학1", "예술대학2", "글로벌센터"};

    int count = 0;

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

        layout = (LinearLayout) rootView.findViewById(R.id.builds);

        makeButtons();

        return rootView;
    }

    public void makeButtons() {
        // margin이 상하좌우 20dp인 레이아웃 파람
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);  // 마진 값 설정

        // gravity가 수평 중앙 정렬인 레이아웃 파람
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;

        LinearLayout[] layout_hor = new LinearLayout[4];    // 레이아웃 4개를 저장하는 객체 선언
        for (int i = 0; i < 11; i++) {
            if (i % 3 == 0) {
                if (i > 0)
                    count++;
                layout_hor[count] = new LinearLayout(getContext()); // 리니어 레이아웃 생성
                layout_hor[count].setLayoutParams(lp);  // 레이아웃의 파람 설정
                layout_hor[count].setOrientation(LinearLayout.HORIZONTAL);  // 수평방향 설정
                layout.addView(layout_hor[count]);  // 레이아웃에 레이아웃 부착
            }
            buttons[i] = new Button(getContext());  // 버튼 생성
            buttons[i].setText(names[i]);   // 버튼 이름 설정
            buttons[i].setTextSize(20);     // 버튼 글자 크기 설정
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setLayoutParams(params); // 버튼 레이아웃 파람 설정
            buttons[i].setBackgroundResource(R.drawable.rounded_corner);    // 버튼 모양 설정
            layout_hor[count].addView(buttons[i]);  // 버튼을 레이아웃에 부착
        }
        count = 0;
    }
}
