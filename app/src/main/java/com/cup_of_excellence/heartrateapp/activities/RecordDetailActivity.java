package com.cup_of_excellence.heartrateapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.cup_of_excellence.heartrateapp.R;
import com.cup_of_excellence.heartrateapp.entities.Constants;
import com.cup_of_excellence.heartrateapp.helpers.MyDbHelper;

import java.util.Calendar;
import java.util.Locale;

public class RecordDetailActivity extends AppCompatActivity {

    //views
    CircularImageView profileIv;
    private TextView nameTv, emailTv, ageTv, maxHeartRateTv, minTargetTv,
            maxTargetRateTv, addedTimeTv, updateTimeTv;
    //action bar
    private ActionBar actionBar;

    private String recordID;

    //db Helper
    private MyDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        //setting up actionBar with title and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("Record Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get record id from adapter throught intent
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        //init DbHelper
        dbHelper = new MyDbHelper(this);

        //init Views
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        ageTv = findViewById(R.id.ageTv);
        maxHeartRateTv = findViewById(R.id.maxHeartRateTv);
        minTargetTv = findViewById(R.id.minTargetTv);
        maxTargetRateTv = findViewById(R.id.maxTargetTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updateTimeTv = findViewById(R.id.updateTimeTv);

        showRecordDetails();
    }

    private void showRecordDetails() {
       //get records details

        //query to select record based on record id
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + recordID + "\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in whole db for that record
        if(cursor.moveToFirst()){
            do{
                String id = ""+ cursor.getInt(cursor.getColumnIndex(Constants.C_ID));
                String image = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
                String name = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                String email = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_EMAIL));
                String age = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_AGE));
                String maxHeartRate = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_MAX_HEARTRATE));
                String minTargetHeartRate = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_MIN_TARGET_HEARTRATE));
                String maxTargetHeartRate = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_MAX_TARGET_HEARTRATE));
                String timestampAdded = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP));
                String timestampUpdate = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP));

                //Converts timestamp to dd/mm/yyyy hh:mm aa eg. 01/11/2020
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded  = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1);
                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar2.setTimeInMillis(Long.parseLong(timestampUpdate));
                String timeUpdate  = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar2);

                // set Data
                nameTv.setText(name);
                emailTv.setText(email);
                ageTv.setText(age);
                maxHeartRateTv.setText(maxHeartRate);
                minTargetTv.setText(minTargetHeartRate);
                maxTargetRateTv.setText(maxTargetHeartRate);
                addedTimeTv.setText(timeAdded);
                updateTimeTv.setText(timeUpdate);
                //if user doesnt attach image then imagUri will be null, so set a default image in the case
                if(image.equals("null")){
                    // no image in record
                    profileIv.setImageResource(R.drawable.ic_person_black);
                }else{
                    //have image in record
                    profileIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }
        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }
}