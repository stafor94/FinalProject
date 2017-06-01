package com.stafor.gachonclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner mSpinner;
    ArrayAdapter<String> mSpinnerAdapter = null;
    EditText edit_name;
    RadioGroup radioGroup;
    Button okBtn, cancelBtn;
    private DBHelper_Profile dbHelper;
    int grade = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DBHelper_Profile(this);

        edit_name = (EditText) findViewById(R.id.editName);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // 키보드 사라지게 하기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);

                if (checkedId == R.id.rb_1)
                    grade = 1;
                else if (checkedId == R.id.rb_2)
                    grade = 2;
                else if (checkedId == R.id.rb_1)
                    grade = 3;
                else if (checkedId == R.id.rb_4)
                    grade = 4;
                else if (checkedId == R.id.rb_5)
                    grade = 5;
            }
        });

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.major));
        // 안드로이드 values 폴더에 arrays에 셋팅된 List를 Adapter에 셋팅 해준다.
        // getResources() 메서드는 리소스 사용에 관한 메서드로 Activity 상속시 사용할 수 있다.

        // Spinner 클릭시 DropDown 모양을 설정 할 수 있다.
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);   // 스피너에 어답터를 연결 시켜 준다.
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            finish();
        }
        if (v.getId() == R.id.btn_ok) {
            if (edit_name.getText().toString().length() < 2 || grade == 0 || mSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "다시 한번 확인해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                String name = edit_name.getText().toString().trim();    // 공백 제거
                String major = mSpinner.getSelectedItem().toString().trim();
                dbHelper.insert(name, Integer.toString(grade), major);
                MainActivity.name = dbHelper.printData(1);
                MainActivity.major = dbHelper.printData(3);

                Intent myIntent = new Intent();
                setResult(RESULT_OK, myIntent);
                finish();
            }
        }
    }
}
