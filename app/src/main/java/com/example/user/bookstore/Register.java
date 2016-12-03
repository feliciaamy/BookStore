package com.example.user.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;

import java.util.concurrent.ExecutionException;

public class Register extends Activity {

    private boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void backToLogin(View v) {
        Intent main = new Intent(this, Login.class);
        startActivity(main);
    }

    public void onSubmit(View w) {
        System.out.println("Clicked");
        EditText fullname = (EditText) findViewById(R.id.fullname);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        EditText address = (EditText) findViewById(R.id.address);
        EditText phone = (EditText) findViewById(R.id.phone);
        EditText creditCard = (EditText) findViewById(R.id.creditcard);


        String fullname_str = fullname.getText().toString();
        String username_str = username.getText().toString();
        String password_str = password.getText().toString();
        String address_str = address.getText().toString();
        String phone_str = phone.getText().toString();
        String creditCard_str = creditCard.getText().toString();
        Log.i("Register", "Clicked");

        try {
            Database database = new Database();
            String result = database.execute(Action.REGISTER.toString(), fullname_str, username_str, password_str,
                    address_str, phone_str, creditCard_str).get();
            if (result.contains("Welcome")) {
                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                backToLogin(w);
            } else {
                Toast.makeText(getApplicationContext(), "Registration not successful", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
