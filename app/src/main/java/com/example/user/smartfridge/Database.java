package com.example.user.smartfridge;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 18/11/16.
 */

public class Database extends AsyncTask<String, Void, String> {

    private String LOGIN_PHP = "indobookstore.org/login.php";
    private String REGISTER_PHP = "indobookstore.org/register.php";
    private HttpURLConnection httpURLConnection;
    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;


    @Override
    protected void onPreExecute() {
        // Domain name to PHP script
    }

    @Override
    protected String doInBackground(String... args) {
        String data_string;
        URL url;
        try {
            if (args[0].equals(Action.LOGIN.toString())) {
                String username = args[1];
                String password = args[2];
                url = new URL(LOGIN_PHP);

                //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

                //Log.i("debug:", name);
                data_string = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

//            } else if (args[0].equals(Action.REGISTER.toString())) {
            } else {
                String fullname = args[1];
                String username = args[2];
                String password = args[3];
                String address = args[4];
                String phone = args[5];
                String creditCard = args[6];
                url = new URL(REGISTER_PHP);
                data_string = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(fullname, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("creditCard", "UTF-8") + "=" + URLEncoder.encode(creditCard, "UTF-8");
            }

            connectHTTP(url);
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();

            return "One Row of data inserted";


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "The URL given is not available";
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to make an HTTP connection";
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    private void connectHTTP(URL url) {
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPostExecute(String result){
//        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
    }
}