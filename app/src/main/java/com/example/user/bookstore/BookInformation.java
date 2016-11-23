package com.example.user.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class BookInformation extends Activity {
    private String ISBN13;
    private TextView title, author, publisher, year, price, stock, subject, page, language, format, isbn13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);
        assignTextView();

        Intent intent = getIntent();
        ISBN13 = intent.getStringExtra("isbn13");
        Log.d("ISBN13", ISBN13);

        try {
            Database database = new Database(this);
            String result = database.execute(Action.GETINFO.toString(), ISBN13).get();
            String[] info = result.split(";");
            Log.d("Book Info", result);
            if (info.length == 11) {
                title.setText(info[0]);
                author.setText("Authors: " + info[1]);
                publisher.setText("Publisher: " + info[2]);
                price.setText(info[3] + " USD");
                stock.setText(info[4] + " stock left");
                isbn13.setText("ISBN13: " + info[5]);
                format.setText("Format: " + info[6]);
                page.setText("Number of Pages: " + info[7]);
                language.setText("Language: " + info[8]);
                year.setText("Publish Year: " + info[9]);
                subject.setText("Subject: " + info[10]);
            } else {
                Toast.makeText(getApplicationContext(), "Unable to get the book information", Toast.LENGTH_LONG).show();
                Intent previousIntent = new Intent(this, BookListActivity.class);
                startActivity(previousIntent);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void assignTextView() {
        title = (TextView) findViewById(R.id.book_title);
        author = (TextView) findViewById(R.id.book_author);
        publisher = (TextView) findViewById(R.id.book_publisher);
        year = (TextView) findViewById(R.id.book_year);
        price = (TextView) findViewById(R.id.book_price);
        stock = (TextView) findViewById(R.id.book_stock);
        subject = (TextView) findViewById(R.id.book_subject);
        page = (TextView) findViewById(R.id.book_page);
        language = (TextView) findViewById(R.id.book_language);
        format = (TextView) findViewById(R.id.book_format);
        isbn13 = (TextView) findViewById(R.id.book_isbn13);
    }

}
