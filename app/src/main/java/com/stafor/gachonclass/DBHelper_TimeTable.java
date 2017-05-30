package com.stafor.gachonclass;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBHelper_TimeTable extends SQLiteOpenHelper {
    public static final String ROOT_DIR = "/data/data/com.stafor.gachonclass/databases/";  //로컬db 저장
    private static final String DATABASE_NAME = "CLASS.db"; //로컬db명
    private static final String TABLE_NAME = "ITtable";
    private static final String FILE_NAME = "CLASS.db";
    private static final int SCHEMA_VERSION = 1; //로컬db 버전


    SQLiteDatabase db;
    Cursor cursor;

    public DBHelper_TimeTable(Context context)    {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        setDB(context); // setDB에 context 부여
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets(); //ctx가 없으면 assets폴더를 찾지 못한다.
        File outfile = new File(ROOT_DIR + FILE_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;

        try {
            is = assetManager.open(FILE_NAME, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {}
    }

    // 메인페이지 텍스트뷰 출력하기 위한 printdata
    public String printData(String day, String classRoom, int index, int field) {
        db = getReadableDatabase();
        String str = "";
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE DAY = '" + day + "' AND CLASS = '" + classRoom + "'";

        cursor = db.rawQuery(QUERY, null);
        cursor.moveToFirst();

        for (int i = 0; i < index; i++)
            cursor.moveToNext();

        try {
            str += cursor.getString(field);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
        return str;
    }

    public int checkClassRoom(String day, String classRoom) {
        int count = 0;
        db = getReadableDatabase();
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE DAY = '" + day + "' AND CLASS = '" + classRoom + "'";
        cursor = db.rawQuery(QUERY, null);
        cursor.moveToFirst();

        if (cursor.getColumnCount() == 0)
            count = 0;
        else {
            count++;
            while (cursor.moveToNext())
                count++;
        }
        return count;
    }

}
