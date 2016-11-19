package com.example.user.smartfridge;

import com.example.user.smartfridge.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */

public class Input_activity extends AppCompatActivity {
    public String BARCODE = "";
    public String PRODUCT_NAME = "";
    public String EXPIRE_DATE = "";
    public double INITIAL_WEIGHT = 0;   //Wait for the sensor
    public double CURRENT_WEIGHT = 0;   //Wait for the sensor
    ImageButton view;
    File root = android.os.Environment.getExternalStorageDirectory();
    String path = root.getAbsolutePath()+"/downloads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_activity);
        Intent intent = getIntent();
        BARCODE = intent.getStringExtra("BARCODE");
        PRODUCT_NAME = intent.getStringExtra("PRODUCT_NAME");
        System.out.println(PRODUCT_NAME);
        EditText name = (EditText) findViewById(R.id.editNAME);
        name.setText(PRODUCT_NAME);
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        RadioButton date = (RadioButton) findViewById(R.id.date);
        RadioButton dontcare = (RadioButton) findViewById(R.id.dont_care);
        EditText datePicker = (EditText) findViewById(R.id.editDate);
        switch (v.getId()){
            case R.id.date:
                if(checked){
                    EXPIRE_DATE = datePicker.getText().toString();
                    dontcare.setChecked(false);
                }
                break;
            case R.id.dont_care:
                if(checked){
                    EXPIRE_DATE = "";
                    date.setChecked(false);
                }
                break;
        }
    }

    public void backToMenu(View v){
        Intent main = new Intent(this,FullscreenActivity.class);
        startActivity(main);
    }
    /////////////////////////////////////////////////////////
    /////Input Data to Database
    /////////////////////////////////////////////////////////
    public void putToDatabase(View v){
        EditText editNAME = (EditText) findViewById(R.id.editNAME);
        PRODUCT_NAME= editNAME.getText().toString();

        // TODO: Find the datatype of date

        String initialweight = Double.toString(INITIAL_WEIGHT);
        String currentweight = Double.toString(CURRENT_WEIGHT);

        // Object of background task
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(BARCODE, PRODUCT_NAME,initialweight,currentweight); // Pass data ; BARCODE received from intent

    }

    class BackgroundTask extends AsyncTask<String,Void,String> {

        String add_info_url;


        @Override
        protected void onPreExecute(){
            // Domain name to PHP script
            add_info_url = "http://iotgroup9.comli.com/add_food.php";
        }

        @Override
        protected String doInBackground(String... args) {

            // Define variable
            String barcode;
            barcode = args[0];

            String name;
            name = args[1];

            String initialweight;
            initialweight = args[2];

            String currentweight;
            currentweight = args[3];


            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

                //Log.i("debug:", name);
                String data_string = URLEncoder.encode("barcode", "UTF-8")+"="+URLEncoder.encode(barcode,"UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name,"UTF-8") + "&" +
                        URLEncoder.encode("initialWeight", "UTF-8")+"="+URLEncoder.encode(initialweight,"UTF-8") + "&" +
                        URLEncoder.encode("currentWeight", "UTF-8")+"="+URLEncoder.encode(currentweight,"UTF-8");

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
        }
    }
    public void selectImage(View v) {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Input_activity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String file_name;
                    if (PRODUCT_NAME=="")
                        file_name = "no_name.jpg";
                    else file_name = PRODUCT_NAME+".jpg";

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), file_name);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        view = (ImageButton) findViewById(R.id.addImage);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    view.setImageBitmap(bitmap);

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                view.setImageBitmap(thumbnail);
            }
        }
    }
}
