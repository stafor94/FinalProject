package com.stafor.gachonclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

//매일할일 DBHelper 클래스
public class DBHelper_Bookmark extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;
    Cursor cursor;

    // Database name
    private static final String DATABASE_NAME = "USER.db";
    // Database version
    private static final int DATABASE_VERSION = 20;
    // Table name
    private static final String TABLE_NAME = "bookmark";

    //DBHelper 생성자(Context, DBname, cursor, DBversion)
    public DBHelper_Bookmark(Context context) {
        // 데이터베이스 이름과 버전 정보를 이용하여 상위 생성자 호출
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /* 데이터 베이스 create
    *  - 생성자에서 넘겨 받은 이름의 DB와 버전의 DB가 존재하지 않을 때 호출
    *  - 새로운 데이터 베이스를 생성할때 사용*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Bookmark", "onCreate() 호출");
        /* 테이블을 생성하기 위해 sql문으로 작성하여 execSQL 문 실행 */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, building TEXT, floor TEXT, classroom TEXT);");
    }

     /* 데이터베이스 Version Upgrade
    *  - DB 가 존재하지만 버전이 다르면 호출됨
    *  - DB를 변경하고, 버전읇 변경할때 여러가지 업그레이드 작업 수행 가능*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Bookmark", "onUpgrade() 호출");
        /* 테이블을 업그레이드 하기 위해 SQL문을 작성하여 execSQL문 실행
        *  - 기존의 테이블을 삭제한 후 테이블 재생성*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 테이블의 레코드 insert
    public void insert(String building, String floor, String classroom) {
        db = getWritableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE building = '" + building +
                "' AND CLASSROOM = '" + classroom + "';", null);

        if (cursor.getCount() == 0)
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(null, '" + building +  "', '" + floor + "', '" + classroom + "');");
        else
            Toast.makeText(context, "이미 등록된 강의실 입니다!", Toast.LENGTH_SHORT).show();

        db.close();
    }

    // 테이블의 레코드 delete
    public void delete(int id) {
        db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = " + id + ";");
        db.close();
    }

    // 메인페이지 텍스트뷰 출력하기 위한 printdata
    public String printData(int position, int index) {
        db = getReadableDatabase();
        String str="";

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        cursor.moveToFirst();
        for (int i = 0; i < position; i++)  // position 만큼 커서를 이동
            cursor.moveToNext();

        if (cursor.isAfterLast()) {
            str = "";
        } else if (index == 0) {
            str += Integer.toString(cursor.getInt(index));
        } else {
            str += cursor.getString(index);
        }
        cursor.close();
        return str;
    }

    public int checkTableCount() {
        int count = 0;
        db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
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