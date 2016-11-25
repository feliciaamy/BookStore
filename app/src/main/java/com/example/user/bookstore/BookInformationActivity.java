package com.example.user.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bookstore.BookDetails.BookInformationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookInformationActivity extends Activity {
    BookInformationAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private String ISBN13;
    private TextView title, price, stock;
    private EditText comment;
    private RatingBar rating, book_rate;
    private Button submit_button;
    private String score;
    private boolean allowToComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);


        Intent intent = getIntent();
        ISBN13 = intent.getStringExtra("isbn13");
        Log.d("ISBN13", ISBN13);

        allowToComment = !givenFeedback();
        prepareLayout(allowToComment);


        expListView = (ExpandableListView) findViewById(R.id.information);

        prepareListData();

        listAdapter = new BookInformationAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void onSubmitFeedback(View v) {
        try {
            String remarks = comment.getText().toString();
            Database database = new Database(this);
            String result = null;
            result = database.execute(Action.INPUTFEEDBACK.toString(), ISBN13, Login.USERNAME, remarks, score).get();

            // Update rate indicator
            setRateIndicator();
            Toast.makeText(getApplicationContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
            Log.d("INPUT FEEDBACK", result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void setRateIndicator() {
        Database database = new Database(this);
        try {
            String result = database.execute(Action.GETAVERAGERATE.toString(), ISBN13).get();
            book_rate.setRating(Float.parseFloat(result));
            Log.d("Rate indicator (Actual)", Float.parseFloat(result) + "");
            Log.d("Rate indicator", book_rate.getRating() + "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private boolean givenFeedback() {
        boolean hasGiven = false;
        try {
            Database database = new Database(this);
            String result = database.execute(Action.GIVENFEEDBACK.toString(), ISBN13, Login.USERNAME).get();
            hasGiven = result.contains("TRUE");
            Log.d(Login.USERNAME, String.valueOf(hasGiven));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return hasGiven;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Book Details");

        // Adding child data
        List<String> information = new ArrayList<String>();

        try {
            Database database = new Database(this);
            String result = database.execute(Action.GETINFO.toString(), ISBN13).get();
            String[] info = result.split(";");
            Log.d("Book Info", result);
            if (info.length == 11) {
                title.setText(info[0]);
                price.setText(info[3] + " USD");
                stock.setText(info[4] + " stock left");
                information.add("Authors            : " + info[1]);
                information.add("Publisher          : " + info[2]);
                information.add("ISBN13             : " + info[5]);
                information.add("Format             : " + info[6]);
                information.add("Number of Pages    : " + info[7]);
                information.add("Language           : " + info[8]);
                information.add("Publish Year       : " + info[9]);
                information.add("Subject            : " + info[10]);
            } else {
                Toast.makeText(getApplicationContext(), "Unable to get the book details", Toast.LENGTH_LONG).show();
                Intent previousIntent = new Intent(this, BookListActivity.class);
                startActivity(previousIntent);
            }
            listDataChild.put(listDataHeader.get(0), information);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void prepareLayout(boolean allowToComment) {
        title = (TextView) findViewById(R.id.book_title);
        price = (TextView) findViewById(R.id.book_price);
        stock = (TextView) findViewById(R.id.book_stock);
        submit_button = (Button) findViewById(R.id.feedback_submit);
        // Set Book Rate (Indicator)
        book_rate = (RatingBar) findViewById(R.id.rate_indicator);
        setRateIndicator();

        rating = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.comment);
        if (!allowToComment) {
            Log.d("ALLOW TO COMMENT", "This user is not allowed to give any comment");
//            rating.setWillNotDraw(true);
//            comment.setWillNotDraw(true);
//            submit_button.setWillNotDraw(true);

        } else {
            Log.d("RATINGBAR", "Set listener");
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    score = String.valueOf(rating);
                    Log.d("RATINGBAR", "Change rating to " + score);

                }
            });


        }
    }
}

