package com.example.user.bookstore.BookDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 3/12/16.
 */

public class CommentListActivity extends Activity {
    private RecyclerView commentView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentsAdapter commentsAdapter;
    private List<CommentRow> commentsList = new ArrayList<CommentRow>();
    private String ISBN13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentslist);
        Intent intent = getIntent();
        ISBN13 = intent.getStringExtra("isbn13");
        Log.d("ISBN13", ISBN13);

        // Recycler View Setting
        commentView = (RecyclerView) findViewById(R.id.comment_view);

        commentView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        commentView.setLayoutManager(mLayoutManager);

        try {
            updateList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void updateList() throws InterruptedException {
        commentsAdapter = new CommentsAdapter(this, commentsList);
        commentView.setAdapter(commentsAdapter);
        commentsAdapter.clearAdapter();

        try {
            Database database = new Database();
            String result = database.execute(Action.GETFEEDBACK.toString(), ISBN13, Login.USERNAME).get();
            Log.d("Feedback", result);
            String[] booksString = result.split("<br>");

            for (String book : booksString) {
                CommentRow commentRow = new CommentRow();
                String[] comment_info = book.split(";");
                if (comment_info.length == 6) {
                    commentRow.setUrl(ISBN13);
                    commentRow.setName(comment_info[0]);
                    commentRow.setDate(comment_info[1]);
                    commentRow.setRating(Integer.parseInt(comment_info[2]));
                    commentRow.setComment(comment_info[3]);
                    String[] usefulness = comment_info[4].split(",");
                    commentRow.setUsefulnessFor(Usefulness.UNLIKE, Integer.parseInt(usefulness[0]));
                    commentRow.setUsefulnessFor(Usefulness.NEUTRAL, Integer.parseInt(usefulness[1]));
                    commentRow.setUsefulnessFor(Usefulness.LIKE, Integer.parseInt(usefulness[2]));
                    switch (Integer.parseInt(comment_info[5])) {
                        case 0:
                            commentRow.setUsefulness(Usefulness.NEUTRAL);
                            break;
                        case 1:
                            commentRow.setUsefulness(Usefulness.LIKE);
                            break;
                        case -1:
                            commentRow.setUsefulness(Usefulness.UNLIKE);
                            break;
                        default:
                            commentRow.setUsefulness(Usefulness.NONE);
                            break;

                    }
                    commentsList.add(commentRow);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        commentsAdapter.notifyDataSetChanged();
    }

}
