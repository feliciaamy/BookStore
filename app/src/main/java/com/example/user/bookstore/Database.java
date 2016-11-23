package com.example.user.bookstore;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 18/11/16.
 */

public class Database extends AsyncTask<String, Void, String> {
    Context context;
    //    AlertDialog alertDialog;
    private String LOGIN_PHP = "http://www.indobookstore.org/login.php";
    private String REGISTER_PHP = "http://www.indobookstore.org/register.php";
    private String GETALL = "http://www.indobookstore.org/getbooks.php";
    private HttpURLConnection httpURLConnection;
    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;
    private Action action;
//    public AsyncResponse delegate = null;

//
//    public interface AsyncResponse {
//        void processFinish(String output, Context context);
//    }

    public Database(Context context) {
//        this.delegate = delegate;
        this.context = context;
    }

//    Database(Context ctx, AsyncResponse delegate) {
//        this.delegate = delegate;
//        context = ctx;
//    }

    @Override
    protected void onPreExecute() {
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Login Status");
    }

    @Override
    protected String doInBackground(String... args) {
        String data_string;
        URL url;
        String type = args[0];
        try {
            if (type.equals(Action.LOGIN.toString())) {
                action = Action.LOGIN;
                String username = args[1];
                String password = args[2];
                url = new URL(LOGIN_PHP);

                data_string = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            } else if (args[0].equals(Action.REGISTER.toString())) {
                action = Action.REGISTER;
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
                        URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("creditcard", "UTF-8") + "=" + URLEncoder.encode(creditCard, "UTF-8");
            } else {
                action = Action.GETALL;
                url = new URL(GETALL);
                data_string = "";
            }

            connectHTTP(url);
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result = result + line + "\n";
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

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
            httpURLConnection.setDoInput(true);
            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(String result) {
//        delegate.processFinish(result, context);
    }

}