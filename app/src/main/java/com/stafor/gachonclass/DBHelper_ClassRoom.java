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

/**
 * Created by 황우상 on 2017-05-29.
 */

public class DBHelper_ClassRoom extends SQLiteOpenHelper {
    public static final String ROOT_DIR = "/data/data/com.stafor.gachonclass/databases/";  //로컬db 저장
    private static final String DATABASE_NAME = "CLASSROOMS.db"; //로컬db명
    private static final String TABLE_NAME = "classrooms";
    private static final String FILE_NAME = "CLASSROOMS.db";
    private static final int SCHEMA_VERSION = 2; //로컬db 버전

    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    public DBHelper_ClassRoom(Context context)    {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        setDB(context); // setDB에 context 부여
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(DATABASE_NAME);
        setDB(context);
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
    public String printData(int index, int floor) {
        db = getReadableDatabase();
        String str = "";

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE FLOOR = " + floor + ";", null);
        cursor.moveToFirst();

        for (int i = 0; i < index; i++)
            cursor.moveToNext();

        str += cursor.getString(1);

        cursor.close();
        return str;
    }

    public int checkFloor(int floor) {
        int count = 0;
        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE FLOOR = " + floor + ";", null);
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
