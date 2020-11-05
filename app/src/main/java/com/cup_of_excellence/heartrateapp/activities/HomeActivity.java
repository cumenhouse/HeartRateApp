package com.cup_of_excellence.heartrateapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cup_of_excellence.heartrateapp.R;
import com.cup_of_excellence.heartrateapp.adapter.AdapterRecord;
import com.cup_of_excellence.heartrateapp.entities.Constants;
import com.cup_of_excellence.heartrateapp.entities.ModelRecord;
import com.cup_of_excellence.heartrateapp.helpers.MyDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //views
    private FloatingActionButton addRecordBtn;
    private RecyclerView recordsRv;

    //DB Helper
    private MyDbHelper dbHelper;
    //action Bar
    ActionBar actionBar;
    ///sort optios
    String orderByNewest = Constants.C_ADDED_TIMESTAMP + " DESC ";
    String orderByOldest = Constants.C_ADDED_TIMESTAMP + " ASC ";
    String orderByTitleAsc = Constants.C_NAME + " ASC ";
    String orderByTitleDesc = Constants.C_NAME + " DESC ";

    //for refreshing records , refresh with last chiisen sort option
    String currentOrderByStatus = orderByNewest;

    /*----For storage permission----*/
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        actionBar = getSupportActionBar();
        actionBar.setTitle("All Records");

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //init views
        addRecordBtn = findViewById(R.id.addRecordBtn);
        recordsRv = findViewById(R.id.recordsRv);

        //init db helper class
        dbHelper = new MyDbHelper(this);

        //loads records (by default newest first)
        loadRecords(orderByNewest);
        //click to start add record activity
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //  startActivity(new Intent(HomeActivity.this, AddUpdateRecordActivity.class));
                Intent intent = new Intent(HomeActivity.this, AddUpdateRecordActivity.class);
                intent.putExtra("isEditMode", false);//want to add new data, set false
                startActivity(intent);
            }
        });
    }

    private boolean checkStoragePermission(){
        //check id storage is enabled or not and return true/false
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    private void requestStoragePermissionImport(){
        //request storage permission for import
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_IMPORT);
    }
    private void requestStoragePermissionExport(){
        //request storage permission for export
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_EXPORT);
    }

   /* private void importCSV() {
        //use same path and file name to import
        String filePathAndName = Environment.getExternalStorageDirectory()+"/HeartRateBackup/" + "HeartRate_Backup.csv";

        File csvFile = new File(filePathAndName);
        //check if exists or not
        if(csvFile.exists()){
            //Backup exist
            try {
                CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));

                String[] nextLine;
                while ((nextLine = csvReader.readNext()) !=null){
                    // use same order for import as used for export eg id is saved on 0index
                    String ids = nextLine[0];
                    String name = nextLine[1];
                    String email = nextLine[2];
                    String dob = nextLine[3];
                    String image = nextLine[4];
                    String addetime = nextLine[5];
                    String updatetime = nextLine[6];
                    String age = nextLine[7];
                    String maxheartrate = nextLine[8];
                    String mintargetheartrate = nextLine[9];
                    String maxtargetheartrate = nextLine[10];


                    long timestamp = System.currentTimeMillis();
                    long id = dbHelper.insertRecord(""+name,
                            ""+email,
                            ""+dob,
                            ""+image,
                            ""+timestamp, //added time will be same
                            ""+timestamp,//updated time will be change
                            ""+age,
                            ""+maxheartrate,
                            ""+mintargetheartrate,
                            ""+maxtargetheartrate
                    );
                }
                Toast.makeText(this, "Backup Restored", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else{
            //backup doesnt exist
            Toast.makeText(this, "No backup found", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*private void exportCSV() {
        //path of csv file
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "HeartRateBackup"); // folder name of backup

        boolean isFolderCreated =false;
        if(!folder.exists()){
            isFolderCreated = folder.mkdir(); //create folder if not exist
        }
        Log.d("CSV_TAG", "exportCSV: "+isFolderCreated);
        //file name
        String csvFileName = "HeartRate_Backup.csv";
        //complete path and name
        String filePathAndName = folder.toString() + "/" + csvFileName;

        //get records
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        recordsList.clear();
        recordsList = dbHelper.getAllRecords(orderByOldest);

        try {
            //write file csv
            FileWriter fw = new FileWriter(filePathAndName);
            for(int i=0; i<recordsList.size(); i++){

                fw.append(""+recordsList.get(i).getId());//id
                fw.append(",");
                fw.append(""+recordsList.get(i).getName());//name
                fw.append(",");
                fw.append(""+recordsList.get(i).getEmail());//email
                fw.append(",");
                fw.append(""+recordsList.get(i).getDob());//dob
                fw.append(",");
                fw.append(""+recordsList.get(i).getImage());//image
                fw.append(",");
                fw.append(""+recordsList.get(i).getAge());//age
                fw.append(",");
                fw.append(""+recordsList.get(i).getMaxHeartRate());//max heart rate
                fw.append(",");
                fw.append(""+recordsList.get(i).getMintargetHeartRate());//min target
                fw.append(",");
                fw.append(""+recordsList.get(i).getMaxtargetHeartRate());//max target heart
                fw.append(",");
                fw.append(""+recordsList.get(i).getAddedTime());//added Time
                fw.append(",");
                fw.append(""+recordsList.get(i).getUpdateTime());//update time
                fw.append(",");
            }
              fw.flush();
            fw.close();

            Toast.makeText(this, "Backup Exported to: " +filePathAndName, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
               //if there is any exception
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private void loadRecords( String orderBy) {
        currentOrderByStatus = orderBy;
        AdapterRecord adapterRecord = new AdapterRecord(HomeActivity.this,
                dbHelper.getAllRecords(orderBy));
        recordsRv.setAdapter(adapterRecord);

        //set num of records
        actionBar.setSubtitle("Total: "+dbHelper.getRecordsCount());
    }

    private void searchRecords(String query){

        AdapterRecord adapterRecord = new AdapterRecord(HomeActivity.this,
                dbHelper.searchRecords(query));
        recordsRv.setAdapter(adapterRecord);
    }

    private void sortOptionItemSelected() {
        //optios to display in dialog
        String[] options = {"Title Ascending","Title Descending","Newest","Oldest"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle option click
                if(which == 0){
                    //title asce
                    loadRecords(orderByTitleAsc);
                }
                else if(which == 1){
                    //title desc
                    loadRecords(orderByTitleDesc);
                }
                else if(which == 2){
                    //newest
                    loadRecords(orderByNewest);
                }
                else if(which == 3){
                    //oldest
                    loadRecords(orderByOldest);
                }
            }
        })
                .create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecords(currentOrderByStatus); //refresh records list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflata menu
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //searchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //search when button on keyboard clicked
                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search sa you type
                searchRecords(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle menu items
        int id = item.getItemId();
        if(id == R.id.action_sort){
            //show sort options (show dialog)
             sortOptionItemSelected();
        }
        else if(id == R.id.action_delete_all){
            //delete all records
          dbHelper.deleteAllData();
          onResume();
        }
       /* else if(id == R.id.action_backup){
            //backup all records to csv file
            if(checkStoragePermission()){
                // permission allowed
                exportCSV();
            }else{
               // permission no allowed
                requestStoragePermissionExport();
            }
        }
        else if(id == R.id.action_restore){
            //restore all records
            if(checkStoragePermission()){
                // permission allowed
                importCSV();
                onResume();
            }else{
                // permission no allowed
                requestStoragePermissionImport();
            }
        }
        */
        else if(id == R.id.action_exit){

            //show exit options (show dialog)
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
  /*  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //handle permission result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case STORAGE_REQUEST_CODE_EXPORT:{

                if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                       //permission granted
                    exportCSV();
                }
                else{
                    //permission denied
                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE_IMPORT:{

                if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                        //pernission granted
                    importCSV();
                }
                else{
                    //permission denied
                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }*/

}