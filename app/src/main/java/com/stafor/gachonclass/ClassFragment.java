package com.stafor.gachonclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class ClassFragment extends Fragment {
    GridLayout layout;
    ArrayList<Integer> list = new ArrayList<>();
    Button[] buttons;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_class, container, false);
        layout = (GridLayout) rootView.findViewById(R.id.layout);

        init();

        return rootView;
    }

    public void init() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            list = bundle.getIntegerArrayList("list");
            buttons = new Button[list.size()];
        }
        for (int i = 0; i < list.size(); i++) {
            buttons[i] = new Button(getContext());
            buttons[i].setText(Integer.toString(list.get(i)));
            layout.addView(buttons[i]);
        }
    }
}
