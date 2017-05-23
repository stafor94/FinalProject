package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    Button searchBtn;
    TextView textView;
    Spinner buildingSp, stairSp;
    String building = "가천관", stair = "1층";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        searchBtn = (Button) rootView.findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClass();
            }
        });
        buildingSp = (Spinner) rootView.findViewById(R.id.spinner);
        buildingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setChecked();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stairSp = (Spinner) rootView.findViewById(R.id.spinner2);
        stairSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setChecked();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void searchClass() {
        Toast.makeText(getContext(), "건물 = " + building + "\n층수 = " + stair, Toast.LENGTH_SHORT).show();
    }

    public void setChecked() {
        if (buildingSp.getSelectedItemId() > 0) {
            building = (String) buildingSp.getAdapter().getItem(buildingSp.getSelectedItemPosition());
        }
        if (stairSp.getSelectedItemId() > 0) {
            stair = (String) stairSp.getAdapter().getItem(stairSp.getSelectedItemPosition());
        }
    }
}
