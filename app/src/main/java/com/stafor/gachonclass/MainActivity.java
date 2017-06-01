package com.stafor.gachonclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    CampusFragment campusFrag;
    RecentFragment recentFrag;
    BookmarkFragment bookmarkFrag;
    SettingsFragment settingsFrag;

    private DBHelper_Profile dbHelper;
    SQLiteDatabase db;
    public static String name = "";
    public static String major = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashAcitivty.class)); // 스플래시 화면을 보여준다

        dbHelper = new DBHelper_Profile(this);
        db = dbHelper.getReadableDatabase();
        name = dbHelper.printData(1);
        major = dbHelper.printData(3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   // 메서드를 사용해 액션바로 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        // 앱 실행 시 홈 화면을 보여준다
        campusFrag = new CampusFragment();
        recentFrag = new RecentFragment();
        bookmarkFrag = new BookmarkFragment();
        settingsFrag = new SettingsFragment();
        // 기본적으로 홈 화면을 보여줌
        getSupportFragmentManager().beginTransaction().add(R.id.container, campusFrag).commit();

        //TabLayout의 addTab() 메서드를 사용하여 탭 버튼을 추가
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("캠퍼스 맵"));
        tabs.addTab(tabs.newTab().setText("즐겨찾기"));
        tabs.addTab(tabs.newTab().setText("최근조회"));
        tabs.addTab(tabs.newTab().setText("설정"));

        //탭에 OnTabSelectedListener 설정
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //탭 버튼을 선택할 때마다 onTabSelected() 호출되어 현재 선택된 탭 객체를 전달
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //댑의 position 값을 가져와서 해당 프래그먼트를 selected 객체에 설정
                int position = tab.getPosition();
                Fragment selected = null;

                if (position == 0) {
                    selected = campusFrag;
                } else if (position == 1) {
                    selected = bookmarkFrag;
                } else if (position == 2) {
                    selected = recentFrag;
                } else if (position == 3) {
                    selected = settingsFrag;
                }
                //선택된 프래그먼트를 메인 액티비티의 contained에 담아서 표시
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 취소키를 누르면 다이어로그 창을 띄움
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("종료하기")
                    .setMessage(R.string.finish)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);	// protect Other Activity after this Activity finish
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moveTaskToBack(true);	// protect Other Activity after this Activity finish
        finish();
    }
}
