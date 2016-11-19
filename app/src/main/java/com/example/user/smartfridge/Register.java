package com.example.user.smartfridge;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void backToLogin() {
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

        Log.i("Register", "Clicked");
        Database database = new Database();
        database.execute(Action.REGISTER.toString(), fullname.toString(), username.toString(),
                password.toString(), address.toString(), phone.toString(), creditCard.toString());
        Toast.makeText(getApplicationContext(),"Registration successful", Toast.LENGTH_LONG).show();
        backToLogin();
    }
}
