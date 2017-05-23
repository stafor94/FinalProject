package com.stafor.gachonclass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by 황우상 on 2017-05-21.
 */

public class SplashAcitivty extends Activity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();       // 2.5 초후 이미지를 닫아버림
            }
        }, 2500);

    }
}
