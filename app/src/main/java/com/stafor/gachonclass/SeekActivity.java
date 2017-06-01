package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SeekActivity extends AppCompatActivity {
    Fragment fragment;
    Button spreadBtn, spread2Btn;
    GridLayout layout;
    ImageView imageView;
    TextView floorTv;

    int floor = 1;
    String building;
    Button[] floorBtns = new Button[15];
    boolean[] isSpeard = {true, false};

    int count;  //건물 층수
    int select; // 선택한 층수

    int sectionItImage[] = {R.drawable.it_1,R.drawable.it_2, R.drawable.it_3,R.drawable.it_4,R.drawable.it_5, R.drawable.it_6};   //it 건물 단면도

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seek);
        this.setFinishOnTouchOutside(false); // 바깥영역 터치로 인한 종료 방지

        setUp();
        switch (building)       //메인에서 누른 건물에 따라 기본 단면도 이미지 설정.
        {
            case  "IT대학":
                imageView.setImageResource(sectionItImage[0]);
            break;
        }
    }

    public void setUp() {
        floorTv = (TextView) findViewById(R.id.textView);
        layout = (GridLayout) findViewById(R.id.layout_floors);
        imageView = (ImageView) findViewById(R.id.imageView);
        spreadBtn = (Button) findViewById(R.id.btn_spread);
        spread2Btn = (Button) findViewById(R.id.btn_spread2);
        spreadBtn.setOnClickListener(myListener);
        spread2Btn.setOnClickListener(myListener);
        imageView.setOnClickListener(myListener);

        Intent myIntent = getIntent();
        building = myIntent.getStringExtra("building");
        floorTv.setText(building + " " + floor + "F");
        createFloors(building);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_spread) {
                if (isSpeard[0]) {
                    layout.setVisibility(View.GONE);
                    spreadBtn.setText("층수▽");
                } else {
                    layout.setVisibility(View.VISIBLE);
                    spreadBtn.setText("층수△");
                }
                isSpeard[0] = !isSpeard[0];
            } else if (v.getId() == R.id.btn_spread2) {
                if (isSpeard[1]) {
                    imageView.setVisibility(View.GONE);
                    spread2Btn.setText("단면도▽");
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    spread2Btn.setText("단면도△");
                }
                isSpeard[1] = !isSpeard[1];
            } else if (v.getId() == R.id.imageView && building.equals("IT대학")) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra("img", sectionItImage[select]);
                startActivity(intent);
            }
        }
    };

    public void createFloors(String building) {
        if (building.equals("가천관"))
            createButtons(-3, 7);
        else if (building.equals("비전타워"))
            createButtons(1, 6);
        else if (building.equals("공과대학1"))
            createButtons(1, 5);
        else if (building.equals("공과대학2"))
            createButtons(1, 5);
        else if (building.equals("바이오나노대학"))
            createButtons(-2, 4);
        else if(building.equals("법과대학"))
            createButtons(1, 6);
        else if(building.equals("IT대학"))
            createButtons(1, 6);
        else if(building.equals("한의과대학"))
            createButtons(1, 5);
        else if(building.equals("예술대학1"))
            createButtons(1, 5);
        else if(building.equals("예술대학2"))
            createButtons(1, 5);
        else if(building.equals("글로벌센터"))
            createButtons(1, 6);
    }

    public void createButtons(int first, int last) {
        count = 0;
        for (int i = first; i <= last; i++) {
            floorBtns[count] = new Button(this);

            if (i == 0) // 0층은 없다
                continue;
            if (i < 0) {
                floorBtns[count].setText("B" + Integer.toString(-i));   // 지하
            } else if (i > 0) { // 지상
                floorBtns[count].setText(Integer.toString(i));
            }
            layout.addView(floorBtns[count]);

            floorBtns[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((Button)v).getText().toString().charAt(0) == 'B') {  // 지하인지 지상인지 구분
                        floor = (((Button) v).getText().toString().charAt(1)) - 48; // 아스키코드값
                        floorTv.setText(building + " B" + floor + "F");
                    }
                    else {
                        floor = (((Button) v).getText().toString().charAt(0)) - 48;
                        floorTv.setText(building + " " + floor + "F");
                    }
                    if(building.equals("IT대학")) {
                        select = Integer.parseInt(((Button) v).getText().toString()) - 1;    // [] 0 ~ 5
                        imageView.setImageResource(sectionItImage[select]); //층수 누를때마다 단면도 바뀌게

                        fragment = new ClassFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("building", building);
                        bundle.putInt("floor", floor);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                    }
                }
            });
            count++;
        }
    }

}
