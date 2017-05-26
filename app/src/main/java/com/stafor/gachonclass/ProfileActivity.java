package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner mSpinner;
    ArrayAdapter<String> mSpinnerAdapter = null;
    EditText edit_name;
    Button okBtn, cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit_name = (EditText) findViewById(R.id.editName);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
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
            Toast.makeText(this, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent();
            myIntent.putExtra("name", edit_name.getText().toString());
            setResult(RESULT_OK, myIntent);
            finish();
        }
    }
}
