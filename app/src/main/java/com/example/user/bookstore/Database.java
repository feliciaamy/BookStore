package com.example.user.bookstore;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 18/11/16.
 */

public class Database extends AsyncTask<String, Void, String> {
    Context context;
    //    AlertDialog alertDialog;
    private String LOGIN_PHP = "http://www.indobookstore.org/login.php";
    private String REGISTER_PHP = "http://www.indobookstore.org/register.php";
    private String GETALL = "http://www.indobookstore.org/getbooks.php";
    private String GETINFO = "http://www.indobookstore.org/getbookinfo.php";
    private String GIVENFEEDBACK = "http://www.indobookstore.org/givenfeedback.php";
    private String INPUTFEEDBACK = "http://www.indobookstore.org/inputfeedback.php";
    private String GETFEEDBACK = "http://www.indobookstore.org/getfeedback.php";
    private String GETAVERAGERATE = "http://www.indobookstore.org/getavgrate.php";
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

            } else if (type.equals(Action.REGISTER.toString())) {
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
            } else if (type.equals(Action.GETALL.toString())) {
                action = Action.GETALL;
                url = new URL(GETALL);
                data_string = "";
            } else if (type.equals(Action.GETINFO.toString())) {
                action = Action.GETINFO;
                url = new URL(GETINFO);
                String isbn13 = args[1];
                data_string = URLEncoder.encode("isbn13", "UTF-8") + "=" + URLEncoder.encode(isbn13, "UTF-8");
            } else if (type.equals(Action.GIVENFEEDBACK.toString())) {
                action = Action.GIVENFEEDBACK;
                url = new URL(GIVENFEEDBACK);
                String isbn13 = args[1];
                String username = args[2];
                data_string = URLEncoder.encode("isbn13", "UTF-8") + "=" + URLEncoder.encode(isbn13, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            } else if (type.equals(Action.INPUTFEEDBACK.toString())) {
                action = Action.INPUTFEEDBACK;
                url = new URL(INPUTFEEDBACK);
                String isbn13 = args[1];
                String username = args[2];
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(c.getTime());
                Log.d("DATE", date);
                String remarks = args[3];
                String score = args[4];
                data_string = URLEncoder.encode("isbn13", "UTF-8") + "=" + URLEncoder.encode(isbn13, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("remarks", "UTF-8") + "=" + URLEncoder.encode(remarks, "UTF-8") + "&" +
                        URLEncoder.encode("score", "UTF-8") + "=" + URLEncoder.encode(score, "UTF-8");
            } else if (type.equals(Action.GETAVERAGERATE.toString())) {
                action = Action.GETAVERAGERATE;
                url = new URL(GETAVERAGERATE);
                String isbn13 = args[1];
                data_string = URLEncoder.encode("isbn13", "UTF-8") + "=" + URLEncoder.encode(isbn13, "UTF-8");
            } else {
                action = Action.GETFEEDBACK;
                url = new URL(GETFEEDBACK);
                String isbn13 = args[1];
                data_string = URLEncoder.encode("isbn13", "UTF-8") + "=" + URLEncoder.encode(isbn13, "UTF-8");
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