package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button homeBtn, classBtn, mypageBtn, settingsBtn;
    HomeFragment homeFrag;

    int state = 0; // 0 = home, 1 = class, 2 = mypage, 3 = settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this,SplashAcitivty.class)); // 스플래시 화면을 보여준다

        homeFrag = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, homeFrag).commit();

        homeBtn = (Button) findViewById(R.id.btn_home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();   // 이전의 프레그먼트를 찾아서 지워준다
                state = 0;
                getSupportFragmentManager().beginTransaction().add(R.id.container, homeFrag).commit();
            }
        });
        classBtn = (Button) findViewById(R.id.btn_class);
        classBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
                state = 1;
            }
        });
        mypageBtn = (Button) findViewById(R.id.btn_mypage);
        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
                state = 2;
            }
        });
        settingsBtn = (Button) findViewById(R.id.btn_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
                state = 3;
            }
        });
    }

    // container에 연결된 프레그먼트를 제거
    public void removeFragment() {
        switch (state) {
            case 0: // home
                getSupportFragmentManager().beginTransaction().remove(homeFrag).commit();
                break;
            case 1: // class
                break;
            case 2: // mypage
                break;
            case 3: // settings
                break;
            default:
                break;
        }
    }
}
