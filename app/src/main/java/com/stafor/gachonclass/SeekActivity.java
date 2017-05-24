package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SeekActivity extends AppCompatActivity {
    Button spreadBtn, spread2Btn;
    LinearLayout layout;
    ImageView imageView;
    boolean[] isSpeard = new boolean[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);

        isSpeard[0] = false;
        isSpeard[1] = false;

        layout = (LinearLayout) findViewById(R.id.layout_stairs);
        imageView = (ImageView) findViewById(R.id.imageView);
        spreadBtn = (Button) findViewById(R.id.btn_spread);
        spreadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSpeard[0]) {
                    isSpeard[0] = false;
                    layout.setVisibility(View.GONE);
                    spreadBtn.setText("층수▽");
                } else {
                    isSpeard[0] = true;
                    layout.setVisibility(View.VISIBLE);
                    spreadBtn.setText("층수△");
                }
            }
        });
        spread2Btn = (Button) findViewById(R.id.btn_spread2);
        spread2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSpeard[1]) {
                    isSpeard[1] = false;
                    imageView.setVisibility(View.GONE);
                    spread2Btn.setText("단면도▽");
                } else {
                    isSpeard[1] = true;
                    imageView.setVisibility(View.VISIBLE);
                    spread2Btn.setText("단면도△");
                }
            }
        });
    }
}
