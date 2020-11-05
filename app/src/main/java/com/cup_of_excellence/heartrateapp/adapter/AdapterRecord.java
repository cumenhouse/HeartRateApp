package com.cup_of_excellence.heartrateapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cup_of_excellence.heartrateapp.R;
import com.cup_of_excellence.heartrateapp.activities.AddUpdateRecordActivity;
import com.cup_of_excellence.heartrateapp.activities.HomeActivity;
import com.cup_of_excellence.heartrateapp.activities.RecordDetailActivity;
import com.cup_of_excellence.heartrateapp.entities.ModelRecord;
import com.cup_of_excellence.heartrateapp.helpers.MyDbHelper;

import java.util.ArrayList;


public class AdapterRecord  extends  RecyclerView.Adapter<AdapterRecord.HolderRecord>{

    //variables
    private Context context;
    private ArrayList<ModelRecord> recordsList;

    //DB Helper
    MyDbHelper dbhelper;

    //constructor


    public AdapterRecord(Context context, ArrayList<ModelRecord> recordsList) {
        this.context = context;
        this.recordsList = recordsList;

        dbhelper = new MyDbHelper(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_record,parent,false);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, final int position) {
        //get data, set data, handel view clics in this method

        //get data
        ModelRecord model = recordsList.get(position);
        final String id = model.getId();
        final String name = model.getName();
        final String email = model.getEmail();
        final String dob = model.getDob();
        final String image = model.getImage();
        final String age = model.getAge();
        final String maxHeartRate = model.getMaxHeartRate();
        final String mintargetHeartRate = model.getMintargetHeartRate();
        final String maxtargetHeartRate = model.getMaxtargetHeartRate();
        final String addedTime = model.getAddedTime();
        final String updateTime = model.getUpdateTime();

        //set data to view
        holder.nameTv.setText(name);
        holder.emailTv.setText(email);
        holder.dobTv.setText(dob);
        //if user doesnt attach image then imagUri will be null, so set a default image in the case
        if(image.equals("null")){
            // no image in record
           holder.profilIv.setImageResource(R.drawable.ic_person_black);
        }else{
         //have image in record
            holder.profilIv.setImageURI(Uri.parse(image));
        }

        //handle item clicks(got to detail record actvity)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // will implement later
                //pass record id to nrxt activity to show details of that record
                Intent intent = new Intent(context, RecordDetailActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);

            }
        });

        //handle more button click listener (show options like edit, delete et)
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show options menu
                showMoreDialog(
                        ""+position,
                        ""+id,
                        ""+name,
                        ""+email,
                        ""+dob,
                        ""+image,
                        ""+age,
                        ""+maxHeartRate,
                        ""+mintargetHeartRate,
                        ""+maxtargetHeartRate,
                        ""+addedTime,
                        ""+updateTime
                );
            }
        });
    }

    private void showMoreDialog(String position, final String id, final String name, final String email, final String dob, final String image,
                                final String age, final String maxHeartRate, final String minTargetHeartRate,
                                final String maxTargetHeartRate, final String addedTime, final String updateTime) {

        //optioms to display in dialog
        String[] options = {"Edit","Delete"};
        //alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //adde itmes to dialogue
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle items clicks
                if(which == 0){
                    //Edit is clicked

                    //start AddupdateRecordActivity to Update existing record
                    Intent intent = new Intent(context, AddUpdateRecordActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("NAME", name);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("DOB", dob);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("AGE", age);
                    intent.putExtra("MAX_HEART_RATE", maxHeartRate);
                    intent.putExtra("MIN_TARGETHEART_RATE", minTargetHeartRate);
                    intent.putExtra("MAX_TARGETHEART_RATE", maxTargetHeartRate);
                    intent.putExtra("ADDED_TIME", addedTime);
                    intent.putExtra("UPDATE_TIME", updateTime);
                    intent.putExtra("isEditMode", true);// need to update existing data, set true
                    context.startActivity(intent);

                }else if(which == 1){
                    //delete is clicked
                    dbhelper.deleteData(id);
                    //refresh recodr by calling activity onResume
                    ((HomeActivity)context).onResume();
                }

            }
        });
        //show dialog
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return recordsList.size(); //return size of list/number or record
    }

    class HolderRecord extends RecyclerView.ViewHolder{

        //views
        ImageView profilIv;
        TextView nameTv, emailTv, dobTv;
        ImageView moreBtn;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);
            profilIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            dobTv = itemView.findViewById(R.id.dobTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }
    }
}
