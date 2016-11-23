package com.example.user.bookstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.user.bookstore.util.SystemUiHider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static String BARCODE = "";
    public static String PRODUCT_NAME = "";

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    //product barcode mode
    public void scanBar(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(FullscreenActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void throwToast(){

//        Toast toast = Toast.makeText(this, "Content:" + BARCODE + " Product:" + PRODUCT_NAME, Toast.LENGTH_SHORT);
//        toast.show();
        try{
            Intent inputActivity = new Intent("Input_activity");

            System.out.println("PRODUCT: " + PRODUCT_NAME);
            System.out.println("BARCODE:" + BARCODE);
            inputActivity.putExtra("PRODUCT_NAME",PRODUCT_NAME);
            inputActivity.putExtra("BARCODE",BARCODE);
            startActivity(inputActivity);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
        }

    }

    public void goToInput(View v){
        try{
            Intent inputActivity = new Intent("Input_activity");
            inputActivity.putExtra(BARCODE,PRODUCT_NAME);
            startActivity(inputActivity);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {
                    //get the extras that are returned from the intent
                    BARCODE = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    getProductName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void getProductName() throws IOException, URISyntaxException {
        String encodedString = URLEncoder.encode(BARCODE, "UTF-8");
        readHTML("https://www.google.com/search?q=" + encodedString);
    }

    public void readHTML(String url) throws IOException, URISyntaxException {
        processCode product = new processCode();
        product.execute(url);
    }

    /** Method to write ascii text characters to file on SD card. Note that you must add a
     WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     a FileNotFound Exception because you won't have write permission. */

    private void writeToSDFile(String txt){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath()+"/downloads");
        dir.mkdirs();
        File file = new File(dir, "HTML.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(txt);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), root.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public String ProductName(String html){
        List<List<String>> names = new ArrayList<List<String>>();
        int index;
        String temp = "";
        String product = "";

        while((index = html.indexOf("class=\"g\"")) != -1 ){
            index = html.indexOf("class=\"g\"");
            temp = html.substring(index,html.indexOf("</h3>"));
            // System.out.println(temp);
            temp = temp.substring(temp.lastIndexOf("\">")+2,temp.length()-4);
            // System.out.println(temp);
            html = html.substring(html.indexOf("</h3>")+3,html.length());
            String[] array = temp.split(" ");
            names.add(Arrays.asList(array));
        }

        for (int i = 0; i < names.size(); i++){
            List<String> sentence = names.get(i);
            for(int c = 0; c < sentence.size();c++){
                String word = sentence.get(c);
                System.out.println(word);
                if(containsCaseInsensitive(product,word) || word.contains(BARCODE) || word.contains("<b>")) break;
                else if (names.size()==1)
                    product += word + " ";
                else{
                    for (int j = i+1; j < names.size(); j++){
                        if(containsCaseInsensitive(word,names.get(j))){
                            int ind = names.get(j).indexOf(word);
                            product += word + " ";
                            break;
                        }
                    }
                }
            }
        }
//        throwToast();
        return product.toUpperCase();
    }

    public boolean containsCaseInsensitive(String s, String l){
        s = s.toLowerCase();
        l = l.toLowerCase();
        return s.contains(l);
    }

    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    private class processCode extends AsyncTask<String, Void, Void> {
        String textResult = "";

        @Override
        protected Void doInBackground(String... textSource) {
            try {
                URL url = new URL(textSource[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(60000);
                connection.setReadTimeout(60000);
                connection.addRequestProperty("User-Agent", "Chrome Dev");
                InputStream in = connection.getInputStream();
                Scanner readehr = new Scanner(connection.getInputStream(), "UTF-8");
                while (readehr.hasNextLine()) {
                    final String line = readehr.nextLine();
                    textResult = textResult + line;
                }
                readehr.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            writeToSDFile(textResult);
            PRODUCT_NAME = ProductName(textResult);
            throwToast();
            super.onPostExecute(aVoid);
        }
    }

}