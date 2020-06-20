package com.example.smartbycicylelock.InnerDB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartbycicylelock.Usermodule.StartJoin;
import com.example.smartbycicylelock.Usermodule.StartLogin;
import com.example.smartbycicylelock.activity.JoinActivity;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Bicycle";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private Context mCtx;
    private boolean correct = false;
    private String password;

    public DBOpenHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

        // 생성자


        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Database.CreateDB._CREATE); // User Table 생성
            db.execSQL(Database.CreateDB._CREATE2); // Device Table 생성

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+Database.CreateDB._TABLENAME);
            onCreate(db);
        }


    public void startDB() {
        SQLiteDatabase db = getReadableDatabase();
    } // db를 실행시켜주는 문장(어플 실행시 넣으면 됨)

    public void close(){
        mDB.close();
    }

    //회원가입
    public void InsertUserDB(StartJoin startJoin)
    {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO User ( ");
        sb.append("name, password, email, code ) ");
        sb.append("VALUES ( ?, ?, ?, ?) ");
        db.execSQL(sb.toString(), new Object[]{startJoin.getName(), startJoin.getPassword(), startJoin.getEmail(), startJoin.getCode()});
    }

    //로그인 오류 확인 함수
    public boolean correct_login(StartLogin start)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        cursor= db.rawQuery("SELECT * from User WHERE email = '" + start.getEmail()+"';", null);
        while(cursor.moveToNext()) {
            password = cursor.getString(2);
        }
        if(password.equals(start.getPassword())) {
            correct = true;
        }
            return correct;
    }

    //User정보 확인
    public List get_all()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        cursor= db.rawQuery("SELECT * from User", null);
        UserData userData = null;

        List _UserData = new ArrayList();
        while(cursor.moveToNext())
        {
            userData.setId(cursor.getInt(0));
            userData.setName(cursor.getString(1));
            userData.setPassword(cursor.getString(2));
            userData.setEmail(cursor.getString(3));
            userData.setCode(cursor.getString(4));

            _UserData.add(userData);
        }
        return  _UserData;
    }

}
