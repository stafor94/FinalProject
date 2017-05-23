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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);

        LinearLayout[] layout_hor = new LinearLayout[4];
        layout_hor[count] = new LinearLayout(getContext());
        layout_hor[count].setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(layout_hor[count]);
        for (int i = 0; i < 11; i++) {
            // layoutparams 생성
            buttons[i] = new Button(getContext());
            buttons[i].setText(names[i]);
            buttons[i].setTextSize(20);
            buttons[i].setLayoutParams(params);
            buttons[i].setBackgroundResource(R.drawable.rounded_corner);

            if (i % 3 == 0 && i > 0) {
                count++;
                layout_hor[count] = new LinearLayout(getContext());
                layout_hor[count].setOrientation(LinearLayout.HORIZONTAL);
                layout.addView(layout_hor[count]);
            }
            layout_hor[count].addView(buttons[i]);
        }

        return rootView;
    }
}
