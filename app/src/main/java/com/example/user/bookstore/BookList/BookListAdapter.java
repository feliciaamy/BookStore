package com.example.user.bookstore.BookList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bookstore.BookInformationActivity;
import com.example.user.bookstore.R;

import java.util.List;

/**
 * Created by user on 23/11/16.
 */

public class BookListAdapter extends RecyclerView.Adapter<BooksList> {
    private List<BookRow> bookList;
    private Context context;
    private int focusedItem = 0;

    public BookListAdapter(Context context, List<BookRow> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public BooksList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row, null);
        BooksList holder = new BooksList(v);

        holder.blueprint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Url = isbn13
                TextView url = (TextView) view.findViewById(R.id.url);
                String isbn13 = url.getText().toString();
                Intent intent = new Intent(context, BookInformationActivity.class);
                intent.putExtra("isbn13", isbn13);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(BooksList holder, int position) {
        BookRow book = bookList.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.publisher.setText(book.getPublisher());
        holder.price.setText(book.getPrice());
        holder.stock.setText(book.getStock());
        holder.url.setText(book.getUrl());
    }

    @Override
    public int getItemCount() {
        return (null != bookList ? bookList.size() : 0);
    }

    public void clearAdapter() {
        bookList.clear();
        notifyDataSetChanged();
    }
}
