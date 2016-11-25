package com.example.user.bookstore;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class Login extends Activity {
    public static String USERNAME;
    protected boolean loginSucceed = false;
    File root = android.os.Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Maybe you need intent (just in case)
//        Intent intend = getIntent();

    }

    public void onLogin(View v) {
        try {
            System.out.println("Clicked");
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);

            String username_str = username.getText().toString();
            String password_str = password.getText().toString();

            Database database = new Database(this);
            String result = database.execute(Action.LOGIN.toString(), username_str, password_str).get();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if (result.contains("Welcome")) {
                loginSucceed = true;
                Log.d("Create Intent", "Go to BookListActivity");
                Intent intent = new Intent(this, BookListActivity.class);
                intent.putExtra("FILTER", "*");
                startActivity(intent);
                USERNAME = username_str;
            }
            Log.d("Login Status", loginSucceed ? "Succeed" : "Not Succeed");
        } catch (Exception e) {
            Log.e("Log in Error", e.getMessage());
            Toast.makeText(getApplicationContext(), "Unable to Log in", Toast.LENGTH_LONG).show();
        }
    }

    public void onHaveNoAccount(View v) {
        try {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getApplicationContext(), "Sorry, you are unable to register at this momment.", Toast.LENGTH_LONG).show();
        }
    }

}
