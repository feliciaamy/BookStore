package com.example.user.bookstore.BookDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.R;

import java.util.concurrent.ExecutionException;

public class CommentActivity extends Activity {
    private EditText comment;
    private RatingBar rating;
    private Button submit_button;
    private String ISBN13;
    private String score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        ISBN13 = intent.getStringExtra("isbn13");
        Log.d("ISBN13", ISBN13);
        setLayout();
    }

    private void setLayout() {
        submit_button = (Button) findViewById(R.id.feedback_submit);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.comment);
        Log.d("RATINGBAR", "Set listener");
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                score = String.valueOf(rating);
                Log.d("RATINGBAR", "Change rating to " + score);
            }
        });
    }

    public void onSubmitFeedback(View v) {
        try {
            String remarks = comment.getText().toString();
            Database database = new Database();
            String result = database.execute(Action.INPUTFEEDBACK.toString(), ISBN13, Login.USERNAME, remarks, score).get();

            Toast.makeText(getApplicationContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
            Log.d("INPUT FEEDBACK", result.toString());

            backToBookInformation(v);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "The comment field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToBookInformation(View v) {
        Intent main = new Intent(this, BookInformationActivity.class);
        main.putExtra("isbn13", ISBN13);
        startActivity(main);
    }

}
