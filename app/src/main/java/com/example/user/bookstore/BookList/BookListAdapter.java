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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 23/11/16.
 */

public class BookListAdapter extends RecyclerView.Adapter<BooksList> {
    private List<BookRow> bookList;
    private Context context;
    private int focusedItem = 0;

    private Map<String, Integer> shoppingBag = new HashMap<String, Integer>();

    public BookListAdapter(Context context, List<BookRow> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public BooksList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcard, null);
        final BooksList holder = new BooksList(v);

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

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn13 = holder.url.getText().toString();
                int total = Integer.parseInt(holder.total.getText().toString());
                String stock_string = holder.stock.getText().toString().replace(" stock left", "");
                int stock = Integer.parseInt(stock_string);

                if (stock > total) {
                    updateShoppingBag(isbn13, 1);
                    holder.total.setText((total + 1) + "");
                }

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn13 = holder.url.getText().toString();
                int total = Integer.parseInt(holder.total.getText().toString());
                String stock_string = holder.stock.getText().toString().replace(" stock left", "");
                int stock = Integer.parseInt(stock_string);

                if (total > 0) {
                    updateShoppingBag(isbn13, -1);
                    holder.total.setText((total - 1) + "");
                }
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
        holder.price.setText(book.getPrice() + " USD");
        holder.stock.setText(book.getStock() + " stock left");
        holder.total.setText(book.getTotal() + "");
        holder.url.setText(book.getUrl());

//        if (book.getStock() <= book.getTotal()) {
//            holder.plus.setEnabled(false);
//        }
//        if (book.getStock() <= 0) {
//            holder.minus.setEnabled(false);
//            holder.minus.
//        }
    }

    @Override
    public int getItemCount() {
        return (null != bookList ? bookList.size() : 0);
    }

    public void clearAdapter() {
        bookList.clear();
        notifyDataSetChanged();
    }

    public void setFilter(List<BookRow> bookList) {
        this.bookList = new ArrayList<>();
        this.bookList.addAll(bookList);
        notifyDataSetChanged();
    }

    public void updateShoppingBag(String isbn13, int total) {
        if (shoppingBag.containsKey(isbn13)) {
            int totalSoFar = shoppingBag.get(isbn13);
            shoppingBag.put(isbn13, totalSoFar + total);
        } else {
            shoppingBag.put(isbn13, total);
        }
    }
}
