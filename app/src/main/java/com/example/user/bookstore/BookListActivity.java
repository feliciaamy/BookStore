package com.example.user.bookstore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.bookstore.BookList.BookListAdapter;
import com.example.user.bookstore.BookList.BookRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 23/11/16.
 */

public class BookListActivity extends Activity {
    private List<BookRow> bookslist = new ArrayList<BookRow>();
    private RecyclerView mRecyclerView;
    private BookListAdapter adapter;

    private int counter = 0;
    private String count;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.booksContainer);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        try {
            updateList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void updateList() throws InterruptedException {
        counter = 0;

        adapter = new BookListAdapter(BookListActivity.this, bookslist);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();

        try {
            Database database = new Database(this);
            String result = database.execute(Action.GETALL.toString()).get();
            Log.d("BookListActivity", result);
            String[] booksString = result.split("<br>");

            for (String book : booksString) {
                BookRow bookRow = new BookRow();
                String[] book_info = book.split(";");
                if (book_info.length == 6) {
                    bookRow.setTitle(book_info[0]);
                    bookRow.setAuthor(book_info[1]);
                    bookRow.setPublisher(book_info[2]);
                    bookRow.setPrice(book_info[3] + " USD");
                    bookRow.setStock(book_info[4] + " stock left");
                    bookRow.setUrl(book_info[5]);
                    bookslist.add(bookRow);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }
}
