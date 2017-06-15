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

/*
====================CLASS.db====================
0:_id INTEGER, 1:class TEXT, 2:day TEXT, 3:start TEXT,
4:end TEXT, 5:subject TEXT, 6:professor TEXT, 7:major TEXT
ex)288/613/fri/5/8/기초회로실험1/이달호/전자공학과
 */
public class DBHelper_TimeTable extends SQLiteOpenHelper {
    public static final String ROOT_DIR = "/data/data/com.stafor.gachonclass/databases/";  //로컬db 저장
    private static final String DATABASE_NAME = "CAMPUSDB.db"; //로컬db명
    private static final String TABLE_NAME = "ITclasses";
    private static final String FILE_NAME = "CAMPUSDB.db";
    private static final int SCHEMA_VERSION = 1; //로컬db 버전

    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    public DBHelper_TimeTable(Context context)    {
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
    public String printData(String day, String classRoom, int index, int field) {
        db = getReadableDatabase();
        String str = "";
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE DAY = '" + day + "' AND CLASS = '" + classRoom + "';";

        cursor = db.rawQuery(QUERY, null);
        cursor.moveToPosition(index);
        try {
            str += cursor.getString(field);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
        return str;
    }

    public int checkClassRoom(String day, String classRoom) {
        int count;
        db = getReadableDatabase();
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE DAY = '" + day + "' AND CLASS = '" + classRoom + "';";
        cursor = db.rawQuery(QUERY, null);
        cursor.moveToFirst();

        count = cursor.getCount();
        return count;
    }

    public boolean isInClass(String week, String hour, String classRoom) {
        String str_start, str_end;
        int int_start, int_end, mHour = Integer.parseInt(hour);
        db = getReadableDatabase();
        week = week.toLowerCase();
        String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE DAY = '" + week + "' AND CLASS = '" + classRoom + "';";
        cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                str_start = cursor.getString(3);
                str_end = cursor.getString(4);
                int_start = convert(str_start);
                int_end = convert(str_end);
                if ((int_start == int_end) && int_start == mHour)
                    return true;
                else if (int_start <= mHour && int_end >= mHour)
                    return true;
            }while (cursor.moveToNext());
        }
        return false;
    }

    public int convert(String time) {
        int result;

        if (time.equals("A"))
            return  9;
        else if (time.equals("B"))
            return 11;
        else if (time.equals("C"))
            return 12;
        else if (time.equals("D"))
            return 14;
        else if (time.equals("E"))
            return 15;

        result = Integer.parseInt(time);
        result += 8;

        return result;
    }
}
