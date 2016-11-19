package com.example.user.smartfridge;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends Activity {

    protected String USERNAME = "";
    File root = android.os.Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Maybe you need intent (just in case)
//        Intent intend = getIntent();

    }

    public void onLogin(View v) {
        try{
            System.out.println("Clicked");
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);

            Database database = new Database();
            database.execute(Action.LOGIN.toString(), username.toString(), password.toString());
            Toast.makeText(getApplicationContext(),"Log in successful", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"Unable to Log in", Toast.LENGTH_LONG).show();
        }
    }

    public void onHaveNoAccount(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            Toast.makeText(getApplicationContext(),"Sorry, you are unable to register at this momment.", Toast.LENGTH_LONG).show();
//            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }
}
