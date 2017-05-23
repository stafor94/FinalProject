package com.stafor.gachonclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SigninActivity extends AppCompatActivity {
    Button signinBtn, cancelBtn;
    EditText editName, editStdNum, editPass, editPass2;
    String name, password;
    int stdNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editName = (EditText) findViewById(R.id.edit_name);
        editStdNum = (EditText) findViewById(R.id.edit_stdNum);
        editPass = (EditText) findViewById(R.id.edit_pass);
        editPass2 = (EditText) findViewById(R.id.edit_pass2);

        signinBtn = (Button) findViewById(R.id.btn_signin);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.length() >= 2 && editStdNum.length() >= 9
                        && editPass.length() >= 8) {
                    name = editName.getText().toString();
                    stdNum = Integer.parseInt(editStdNum.getText().toString());
                    if (editPass.getText().toString().equals(editPass2.getText().toString())) {
                        password = editPass.getText().toString();
                        Signin();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.fail_password, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fail_signin, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void Signin() {
        Intent myIntent = getIntent();
        myIntent.putExtra("name", name);
        myIntent.putExtra("stdNum", stdNum);
        myIntent.putExtra("password", password);
        setResult(RESULT_OK, myIntent);
    }
}
