package com.example.user.bookstore.BookList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bookstore.BookDetails.BookInformationActivity;
import com.example.user.bookstore.MainActivity;
import com.example.user.bookstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 23/11/16.
 */

public class BookListAdapter extends RecyclerView.Adapter<BooksList> {
    private Context context;
    private int focusedItem = 0;
    private List<BookRow> bookList;
//    private Map<String, Integer> shoppingBag = new HashMap<String, Integer>();

    public BookListAdapter(Context context, List<BookRow> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public BooksList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcard, null);
        final BooksList holder = new BooksList(v);

        holder.learnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Url = isbn13
                BookRow book = bookList.get(holder.getLayoutPosition());
                String isbn13 = book.getUrl();
//                TextView url = (TextView) view.findViewById(R.id.url);
//                String isbn13 = url.getText().toString();
                Intent intent = new Intent(context, BookInformationActivity.class);
                intent.putExtra("isbn13", isbn13);
                context.startActivity(intent);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int position = MainActivity.bookList.indexOf(holder.getLayoutPosition());
                BookRow book = bookList.get(holder.getLayoutPosition());
                String isbn13 = book.getUrl();
                int total = book.getTotal();
                int stock = book.getStock();

                if (stock > total) {
                    book.setTotal(total + 1);
                    updateShoppingBag(book, 1);
                    holder.total.setText((total + 1) + "");
                }

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookRow book = bookList.get(holder.getLayoutPosition());
                String isbn13 = book.getUrl();
                int total = book.getTotal();
                if (total > 0) {
                    book.setTotal(total - 1);
                    updateShoppingBag(book, -1);
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
        String title = book.getTitle();
        String author = book.getAuthor();
        if (title.length() > 40) {
            title.substring(0, 35);
            title = title + "...";
        } else {
            Log.d("TITLE", title + " " + title.length());
        }
        if (author.length() > 30) {
            author.substring(0, 25);
            author = title + "...";
        } else {
            Log.d("AUTHOR", author + " " + author.length());
        }
        holder.title.setText(title);
        holder.author.setText(book.getAuthor());
        holder.publisher.setText(book.getPublisher());
        holder.price.setText(book.getPrice() + " USD");
        holder.stock.setText(book.getStock() + " stock left");
        holder.total.setText(book.getTotal() + "");
        holder.url.setText(book.getUrl());
    }

    @Override
    public int getItemCount() {
        return (null != MainActivity.bookList ? bookList.size() : 0);
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

    public void updateShoppingBag(BookRow book, int total) {
        if (MainActivity.shoppingBag.containsKey(book)) {
            int totalSoFar = MainActivity.shoppingBag.get(book);
            MainActivity.shoppingBag.put(book, totalSoFar + total);
            if (totalSoFar + total == 0) {
                MainActivity.shoppingBag.remove(book);
            }
        } else {
            MainActivity.shoppingBag.put(book, total);
        }
        Log.d("UPDATE SHOPPING BAG", MainActivity.shoppingBag.toString());
    }
}
