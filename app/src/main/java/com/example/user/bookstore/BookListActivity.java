package com.example.user.bookstore;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.bookstore.BookList.BookListAdapter;
import com.example.user.bookstore.BookList.BookRow;
import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 23/11/16.
 */

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private List<BookRow> bookslist = new ArrayList<BookRow>();
    private RecyclerView mRecyclerView;
    private BookListAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.booksContainer);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        try {
            updateList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onSearchRequested() {
////        pauseSomeStuff();
//        return super.onSearchRequested();
//    }

    public void updateList() throws InterruptedException {
        adapter = new BookListAdapter(BookListActivity.this, bookslist);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();

        try {
            Database database = new Database(this);
            String result = database.execute(Action.GETBOOKS.toString()).get();
            Log.d("BookListActivity", result);
            String[] booksString = result.split("<br>");
            Log.d("TOTAL BOOKS", booksString.length + "");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<BookRow> newList = new ArrayList<BookRow>();
        for (BookRow book : bookslist) {
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            String publisher = book.getPublisher().toLowerCase();
            if (title.contains(newText) || author.contains(newText) || publisher.contains(newText)) {
                newList.add(book);
            }
        }
        adapter.setFilter(newList);
        return false;
    }
}
