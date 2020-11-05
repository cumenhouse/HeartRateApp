package com.cup_of_excellence.heartrateapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cup_of_excellence.heartrateapp.entities.Constants;
import com.cup_of_excellence.heartrateapp.entities.ModelRecord;

import java.util.ArrayList;

//Database Helper Class that contain all crud methods
public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         //create table on that db
        db.execSQL(""+ Constants.CREATE_TABLE);
        db.execSQL(""+ Constants.CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database(if there i any structure change the change db vresion)

        //drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_USER);

        //create table again
        onCreate(db);
    }

    //insert record to db
    public long insertRecord(String name, String email, String dob, String image, String addedTime,
                             String updateTime, String age, String maxHeartRate, String minTargetHeartRate, String maxTargetHeartRate){
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //insert Data
        values.put(""+Constants.C_NAME, name);
        values.put(""+Constants.C_EMAIL, email);
        values.put(""+Constants.C_DOB, dob);
        values.put(""+Constants.C_IMAGE, image);
        values.put(""+Constants.C_ADDED_TIMESTAMP, addedTime);
        values.put(""+Constants.C_UPDATED_TIMESTAMP, updateTime);
        values.put(""+Constants.C_AGE, age);
        values.put(""+Constants.C_MAX_HEARTRATE, maxHeartRate);
        values.put(""+Constants.C_MIN_TARGET_HEARTRATE, minTargetHeartRate);
        values.put(""+Constants.C_MAX_TARGET_HEARTRATE, maxTargetHeartRate);

        //insert row, it will return record id of saved record
        long id = db.insert( ""+Constants.TABLE_NAME, null, values);
        //close db connection
        db.close();
        //return record
        return  id;

    }

    //update record to db
    public void updateRecord(String id, String name, String email, String dob, String image, String addedTime,
                             String updateTime, String age, String maxHeartRate, String minTargetHeartRate, String maxTargetHeartRate){
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //insert Data
        values.put(""+Constants.C_NAME, name);
        values.put(""+Constants.C_EMAIL, email);
        values.put(""+Constants.C_DOB, dob);
        values.put(""+Constants.C_IMAGE, image);
        values.put(""+Constants.C_ADDED_TIMESTAMP, addedTime);
        values.put(""+Constants.C_UPDATED_TIMESTAMP, updateTime);
        values.put(""+Constants.C_AGE, age);
        values.put(""+Constants.C_MAX_HEARTRATE, maxHeartRate);
        values.put(""+Constants.C_MIN_TARGET_HEARTRATE, minTargetHeartRate);
        values.put(""+Constants.C_MAX_TARGET_HEARTRATE, maxTargetHeartRate);

        //update row, it will return record id of saved record
        db.update(Constants.TABLE_NAME, values, Constants.C_ID + " = ? ", new String[] {id});
        //close db connection
        db.close();
    }

    //get all data
    public ArrayList<ModelRecord> getAllRecords(String orderBy){
        //order by query will allow to sort data e.g newest/oldest first , name, ascending/ descending
        // it will return list or records since we have return type ArrayList<ModelRecord>

        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "select * from " + Constants.TABLE_NAME + " order by " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping throuht all record and add to list
        if(cursor.moveToFirst()){

            do{
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_DOB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_AGE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MAX_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MIN_TARGET_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MAX_TARGET_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP))
                        );
                //add record to List
                recordsList.add(modelRecord);
            } while(cursor.moveToNext());
        }
              //close db connection
               db.close();
        //return List
        return  recordsList;
    }

    //search data
    public ArrayList<ModelRecord> searchRecords(String query){
        //order by query will allow to sort data e.g newest/oldest first , name, ascending/ descending
        // it will return list or records since we have return type ArrayList<ModelRecord>

        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "select * from " + Constants.TABLE_NAME + " where " + Constants.C_NAME + " like '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping throuht all record and add to list
        if(cursor.moveToFirst()){

            do{
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_DOB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_AGE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MAX_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MIN_TARGET_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_MAX_TARGET_HEARTRATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP))
                );
                //add record to List
                recordsList.add(modelRecord);
            } while(cursor.moveToNext());
        }
        //close db connection
        db.close();
        //return List
        return  recordsList;
    }

    //delete data using id
    public void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " =? ", new String[]{id});
        db.close();
    }
    //delete all data from table
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" delete from " + Constants.TABLE_NAME);
        db.close();

    }

    //get number of records
    public int getRecordsCount(){
        String countQuery = "select * from " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // transaction for table user
    public Boolean insertDataUser(String username, String password){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();

        contentValues.put(""+Constants.U_USERNAME, username);
        contentValues.put(""+Constants.U_PASSWORD, password);
        //insert row, it will return record id of saved record
        long result = MyDB.insert( ""+Constants.TABLE_USER, null, contentValues);
        MyDB.close();
        if(result==-1)
            return false;
        else
            return true;

    }

    public Boolean checkusername(String username){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from " + Constants.TABLE_USER + " where " + Constants.U_USERNAME + " = ? ", new String[] {username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from " + Constants.TABLE_USER + " where " + Constants.U_USERNAME + " = ? and " + Constants.U_PASSWORD + " = ?", new String[] {username, password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
