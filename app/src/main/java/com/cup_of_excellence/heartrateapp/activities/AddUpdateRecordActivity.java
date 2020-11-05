package com.cup_of_excellence.heartrateapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.cup_of_excellence.heartrateapp.R;
import com.cup_of_excellence.heartrateapp.adapter.AdapterRecord;
import com.cup_of_excellence.heartrateapp.entities.ModelRecord;
import com.cup_of_excellence.heartrateapp.helpers.MyDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddUpdateRecordActivity extends AppCompatActivity {
    //views
    private CircularImageView profileIv;
    private EditText nameEt, emailEt;
    private EditText dobEt;
    private FloatingActionButton saveBtn;

    //date picker
    int year,month,day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    DatePickerDialog datePickerDialog;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    //arrays of permissions
    private String[] cameraPermissions; //camera and storage
    private String[] storagePermissions; // only storage
    //variables(will contain data to save)
     private Uri imageUri;
     private String id, name, email, dob, age, maxHeartRate, minTargetHeartRate, maxTargetHeartRate, addedTime, updateTime;
     private boolean isEditMode  = false;

     //db helper
    private MyDbHelper dbHelper;


    //Action bar
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);

        //init
        actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("Add Record");
        //back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // init Views
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        saveBtn = findViewById(R.id.saveBtn);
        dobEt = findViewById(R.id.dobEt);

        //get Data from intent
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        // set data to views
        if(isEditMode){
            //update data

            actionBar.setTitle("Update Data");
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            email = intent.getStringExtra("EMAIL");
            dob = intent.getStringExtra("DOB");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            age = intent.getStringExtra("AGE");
            maxHeartRate = intent.getStringExtra("MAX_HEART_RATE");
            minTargetHeartRate = intent.getStringExtra("MIN_TARGETHEART_RATE");
            maxTargetHeartRate = intent.getStringExtra("MAX_TARGETHEART_RATE");
            addedTime = intent.getStringExtra("ADDED_TIME");
            updateTime = intent.getStringExtra("UPDATE_TIME");

            //set data to Views
            nameEt.setText(name);
            emailEt.setText(email);
            dobEt.setText(dob);
           //if no image was selected while adding data, imageUri value willl be "null"
            if(imageUri.toString().equals("null")){

                profileIv.setImageResource(R.drawable.ic_person_black);
            }else {
                //have image set
                profileIv.setImageURI(imageUri);
            }

        }else {
            //add data

                actionBar.setTitle("Add Record");
        }

        //init db Helper
         dbHelper = new MyDbHelper(this);
        //init permission arrays
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddUpdateRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int years, int month, int dayOfMonth) {
                        dobEt.setText(dayOfMonth+"/"+(month+1)+"/"+years);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //click image view to show image pick dialog
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                imagePickDialog();
            }
        });

        // click save button to save record
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inputData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    int y = year;
  /*  //calculate max heart rate
    public float maxHeartRate(){
        float rate;
        int aa = calAge();
        rate = 220-aa;
        return  rate;
    }
    //calculate min of target heart rate
    public float minTargetHeartRate(){
        float min;
        min = 0.5f * maxHeartRate();
        return min;
    }
    //calculate max of target heart rate
    public float maxTargetHeartRate(){
        float max;
        max = 0.85f * maxHeartRate();
        return max;
    }*/

    private void inputData() throws ParseException {
        //get Data
        name = ""+nameEt.getText().toString().trim();
        email = ""+emailEt.getText().toString().trim();
        //dob = 2/11/2020
        dob = ""+dobEt.getText().toString().trim();

        java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            Date starDate = df.parse(dob);
            Calendar c = Calendar.getInstance();
            c.setTime(starDate);
            int y = c.get(Calendar.YEAR);

        int calAge = 2020 - y;
        float maxHeartRate = 220-calAge;
        float minTargetHeartRate = 0.5f * maxHeartRate;
        float maxTargetHeartRate = 0.85f * maxHeartRate;


        if(isEditMode){
            //update data

            String timestamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(""+id,
                    ""+name,
                    ""+email,
                    ""+dob,
                    ""+imageUri,
                    ""+addedTime,
                    ""+timestamp,
                    ""+calAge,
                    ""+maxHeartRate,
                    ""+minTargetHeartRate,
                    ""+maxTargetHeartRate);

            Toast.makeText(this, "Record Added against ID: "+ id, Toast.LENGTH_SHORT).show();
            nameEt.setText("");
            emailEt.setText("");
            dobEt.setText("");
        }
        else {
            //new data

            //save to db
            String timestamp = ""+System.currentTimeMillis();

            long id = dbHelper.insertRecord(""+name,
                    ""+email,
                    ""+dob,
                    ""+imageUri,
                    ""+timestamp, //added time will be same
                    ""+timestamp,//updated time will be change
                    ""+calAge,
                    ""+maxHeartRate,
                    ""+minTargetHeartRate,
                    ""+maxTargetHeartRate);

            Toast.makeText(this, "Update... ", Toast.LENGTH_SHORT).show();
            nameEt.setText("");
            emailEt.setText("");
            dobEt.setText("");
            //profileIv.setImageResource(0);
            //profileIv.setBackground(null);
        }

    }

    private void imagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //title
        builder.setTitle("Pick Image From");
        //set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if(which == 0){
                    //camera clicked
                    if(!checkCameraPermissions()){
                        requestCameraPermission();
                    }
                    else {
                        //permission already granted
                        pickFromCamera();
                    }
                }
                else if(which == 1){
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    }
                    else {
                        // //permission already granted
                        pickFromGallery();
                    }
                }
            }
        });
        //create/show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        //intent to pick image gallery, the image will be returned in onActivity result method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");//only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image camera, the image will be returned in onActivity result method
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    //check
    private boolean checkStoragePermission(){
        //check if storage  permission is enable or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  result;

    }

    private void  requestStoragePermission(){
        //request the storage permission
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermissions(){
        //check if camera  permission is enable or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
       return result && result1;
    }
    private void  requestCameraPermission(){
        //request the camera permission
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();// go back by clicking back of actionBar
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //result of permission allowed/denied
        switch (requestCode){

            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean cameraAccepted =  grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted =  grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //both permission allowed
                        pickFromCamera();
                    }
                    else {

                        Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean storageAccepted =  grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        // storage permission allowed
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image picked from camera or gallery will be received here

        if(resultCode == RESULT_OK){
            //image is picked

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //picked from gallery

                //cropp image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //picked from camera

                //cropp image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //cropperd image receiveid
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){

                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set Image
                    profileIv.setImageURI(resultUri);
                }
                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error = result.getError();
                    Toast.makeText(this, ""+ error, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}