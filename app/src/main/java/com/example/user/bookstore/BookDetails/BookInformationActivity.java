package com.example.user.bookstore.BookDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.MainActivity;
import com.example.user.bookstore.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookInformationActivity extends Activity {
    // Information List
    BookInformationAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private boolean allowToComment;
    private String ISBN13;
    private TextView title, price, stock;
    private RatingBar book_rate;
    private Button feedback_button;
    private String score;
    // RecyclerView
    private RecyclerView commentView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentsAdapter commentsAdapter;
    private List<CommentRow> commentsList = new ArrayList<CommentRow>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ISBN13 = intent.getStringExtra("isbn13");
        Log.d("ISBN13", ISBN13);

        allowToComment = !givenFeedback();
        setContentView(R.layout.activity_book_information);

        prepareLayout(allowToComment);
        expListView = (ExpandableListView) findViewById(R.id.information);

        prepareListData();

        listAdapter = new BookInformationAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


//        // Recycler View Setting
//        commentView = (RecyclerView) findViewById(R.id.comment_view);
//
//        commentView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(this);
//        commentView.setLayoutManager(mLayoutManager);
//
//        try {
//            updateList();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void setRateIndicator() {
        Database database = new Database();
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
            Database database = new Database();
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

//    public void updateList() throws InterruptedException {
//        commentsAdapter = new CommentsAdapter(this, commentsList);
//        commentView.setAdapter(commentsAdapter);
//        commentsAdapter.clearAdapter();
//
//        try {
//            Database database = new Database(this);
//            String result = database.execute(Action.GETFEEDBACK.toString(), ISBN13).get();
//            Log.d("Feedback", result);
//            String[] booksString = result.split("<br>");
//
//            for (String book : booksString) {
//                CommentRow commentRow = new CommentRow();
//                String[] comment_info = book.split(";");
//                if (comment_info.length == 5) {
//                    commentRow.setName(comment_info[0]);
//                    commentRow.setDate(comment_info[1]);
//                    commentRow.setRating(Integer.parseInt(comment_info[2]));
//                    commentRow.setComment(comment_info[3]);
////                    TODO: FIX THIS LATER
////                    commentRow.setUsefulness(Double.parseDouble(comment_info[4]));
//                    commentsList.add(commentRow);
//                }
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        commentsAdapter.notifyDataSetChanged();
//    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Book Details");

        // Adding child data
        List<String> information = new ArrayList<String>();

        try {
            Database database = new Database();
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
                Intent previousIntent = new Intent(this, MainActivity.class);
                startActivity(previousIntent);
            }
            listDataChild.put(listDataHeader.get(0), information);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void onFeedbackClicked(View v) {
        if (!allowToComment) {
            Log.d("ALLOW TO COMMENT", "This user is not allowed to give any comment");
            Toast.makeText(getApplicationContext(), "Your feedback has been submitted, you're not allowed to give more feedback", Toast.LENGTH_LONG).show();
        } else {
            goToCommentActivity();
        }
    }

    private void prepareLayout(boolean allowToComment) {
        title = (TextView) findViewById(R.id.book_title);
        price = (TextView) findViewById(R.id.book_price);
        stock = (TextView) findViewById(R.id.book_stock);

        // Set Book Rate (Indicator)
        book_rate = (RatingBar) findViewById(R.id.rate_indicator);
        setRateIndicator();

        feedback_button = (Button) findViewById(R.id.feedback_button);
    }

    public void goToCommentActivity() {
        Intent main = new Intent(this, CommentActivity.class);
        main.putExtra("isbn13", ISBN13);
        startActivity(main);
    }

    public void goToCommentListActivity(View v) {
        Intent main = new Intent(this, CommentListActivity.class);
        main.putExtra("isbn13", ISBN13);
        startActivity(main);
    }
}

