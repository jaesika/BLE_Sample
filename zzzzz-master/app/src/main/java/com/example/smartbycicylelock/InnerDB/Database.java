package com.example.smartbycicylelock.InnerDB;

import android.provider.BaseColumns;

// User Table 생성
public class Database {
    public static final class CreateDB implements BaseColumns {
        // 첫번째 User table
        public static final String _TABLENAME = "User";
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String CODE = "code";

        // 두번째 Device table
        public static final String _TABLENAME_2= "Device";
        public static final String CODE_2 = "code";
        public static final String battery = "battery";
        public static final String status = "status";
        public static final String GPS = "GPS";

        public static final String _CREATE =
                "create table User ( " + "_id integer primary key autoincrement, " +
                        "name not null, " +
                        "password, " +
                        "email unique, " +
                        "code )";

        public static final String _CREATE2 =
                "create table "+_TABLENAME_2+"("
                        + " _id integer primary key autoincrement,"
                        +battery+" not null , "
                        +status+" not null , "
                        +GPS+" not null );";


    }
}
