package com.cup_of_excellence.heartrateapp.entities;

public class Constants {

    // db Name
    public static final String DB_NAME = "MY_RECORDS_DB";
    //dbVersion
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME ="MY_RECORDS_TABLE";
    public static final String TABLE_USER = "USER";
    //columns/fields of table MY_RECORD_TABLE
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_DOB = "DOB";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";
    public static final String C_AGE = "AGE";
    public static final String C_MAX_HEARTRATE = "MAX_HEART_RATE";
    public static final String C_MIN_TARGET_HEARTRATE = "MIN_TARGET_HEART_RATE";
    public static final String C_MAX_TARGET_HEARTRATE = "MAX_TARGET_HEART_RATE";

    // columns/field of table USER
    public static final String U_ID = "ID";
    public static final String U_USERNAME = "USER_NAME";
    public static final String U_PASSWORD = "PASSWORD";

    //create table qwery1

    public static  final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + C_NAME + " TEXT NOT NULL, "
            + C_EMAIL + " TEXT NOT NULL, "
            + C_DOB + " TEXT NOT NULL, "
            + C_IMAGE + " TEXT NOT NULL, "
            + C_ADDED_TIMESTAMP + " TEXT NOT NULL, "
            + C_UPDATED_TIMESTAMP + " TEXT NOT NULL, "
            + C_AGE + " TEXT NOT NULL, "
            + C_MAX_HEARTRATE + " TEXT NOT NULL, "
            + C_MIN_TARGET_HEARTRATE + " TEXT NOT NULL, "
            + C_MAX_TARGET_HEARTRATE + " TEXT NOT NULL "
            + ")";

    //create table qwery2

    public static  final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + U_USERNAME + " TEXT NOT NULL, "
            + U_PASSWORD + " TEXT NOT NULL "
            + ")";
}
