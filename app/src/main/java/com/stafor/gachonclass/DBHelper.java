package com.stafor.gachonclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite 사용할 수 있도록 SQLiteOpenHelper 클래스를 상속받아 DBHelper 생성
 */
public class DBHelper extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db;
    Cursor cursor;

    // DATABASE name
    private static final String DATABASE_NAME = "product.db";
    // DATABASE version
    private static final int DATABASE_VERSION = 1;
    // TABLE name
    private static final String TABLE_NAME = "productTable";

    // DBHelper 생성자(context, DB name, cursor, DB version)
    public DBHelper(Context context) {
        //데이터베이스 이름과 버전 정보를 이용하여 상위 생성자를 호출
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }//end of DBHelper

    /* 데이터베이스 생성(새로운 데이터베이스를 생성할 때 호출됨)
       - 생성자에서 넘겨받은 이름의 DB와 버전의 DB가 존재하지 않을 때 한번 호출됨
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /* 테이블을 생성하기 위해 SQL문을 작성하여 execSQL 문 실행
           - execSQL()메소드는 SELECT 문을 제외한 모든 SQL문을 실행
           - CREATE TABLE 테이블명 (컬럼명 타입 옵션);
        */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " CLASSROOM TEXT, FLOOR INTEGER, MAJOR TEXT);");
    }//end of onCreate

    /*
       데이터베이스 Open(DB를 열 때 호출됨)
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
    }//end of onOpen

    /* 데이터베이스 Version Upgrade(DB가 존재하지만 버전이 다르면 호출됨)
       - DB를 변경하고, 버전을 변경할 때 여러가지 업그레이드 작업 수행 가능
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         /* 테이블을 업그레이드하기 위해 SQL문을 작성하여 execSQL 문 실행
           - DROP TABLE IF EXISTS 테이블명;
           - 기존 테이블을 삭제한 후 테이블 재 생성
        */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }//end of onUpgrade

    //조건에 맞는 레코드 Select
    public String[] select(int floor) {
       //데이터베이스를 read 모드로 open
        db = getReadableDatabase();
        String str[] = {};
        int i = 0;
        /* 레코드를 검색하기 위해 SELECT 문을 작성하여 rawQuery 문 실행
           - rawQuery()는 SELECT(쿼리) 결과를 Cursor 객체에 저장하는 SQL 실행 방법
           - SELECT 검색할 컬럼 FROM 테이블명 WHERE 조건;
           - 조건에 맞는 레코드를 검색할 때 사용
           - 테이블에서 조건에 맞는 레코드를 검색하여 cursor 객체에 저장
             (rawQuery()는 검색 결과를 cursor 객체에 반환)
         */
         cursor = db.rawQuery("SELECT _id, CLASSROOM, FLOOR, MAJOR FROM " + TABLE_NAME
                                + " WHERE FLOOR LIKE '"
                                + Integer.toString(floor)
                                + "%'", null);

        /* 반복문을 사용하여 cursor 객체에 있는 레코드의 컴럼값을 추출
           - moveToNext()는 커서를 다음 레코드로 이동시키는 메소드로, 만약 레코드가 없으면 false 반환
           - 레코드의 컬럼값을 추출할 때는 컬럼의 타입에 따라 getInt(컬럼index), getString(컬럼index)
             등의 메소드 사용
         */
        while (cursor.moveToNext()) {
            str[i++] += cursor.getString(1);   // CLASSROOM
        }

        cursor.close();
        return str;
    }//end of select

    // 테이블에 있는 전체 레코드 Select
    public String printData() {
        db = getReadableDatabase();
        String str = "";

        // SELECT * FROM 테이블명 null;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            str += cursor.getInt(0) // _id
                    + " : 강의실: "
                    + cursor.getString(1)   // CLASSROOM
                    + ", 층수: "
                    + cursor.getInt(2)  // FLOOR
                    + ", 전공: "
                    + cursor.getString(3)   // MAJOR
                    + "\n";
        }
        cursor.close();
        return str;
    }//end of printData
}//end of DBHelper
