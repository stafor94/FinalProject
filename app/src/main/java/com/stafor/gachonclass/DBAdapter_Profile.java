package com.stafor.gachonclass;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

//매일 할일 DBAdapter 커서 클래스
public class DBAdapter_Profile extends CursorAdapter {

    public DBAdapter_Profile(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView textView = (TextView)view.findViewById(R.id.tv_name);
        textView.setText(cursor.getString(0));
    }
}
