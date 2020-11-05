package com.cup_of_excellence.heartrateapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cup_of_excellence.heartrateapp.R;
import com.cup_of_excellence.heartrateapp.helpers.MyDbHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private TextView account;
    private Button btnlogin;
    MyDbHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        username = (EditText)findViewById(R.id.username1);
        password = (EditText)findViewById(R.id.password1);
        btnlogin = (Button)findViewById(R.id.btnsignin1);
        account = (TextView)findViewById(R.id.account);
        DB = new MyDbHelper(this);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Pleae enter all the fields", Toast.LENGTH_SHORT).show();

                else {

                    Boolean checkuserpass = DB.checkusernamepassword(user,pass);
                    if(checkuserpass==true){

                        Toast.makeText(LoginActivity.this, "Sign in Succefully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }else{

                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}