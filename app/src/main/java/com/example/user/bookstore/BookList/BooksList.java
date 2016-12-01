package com.example.user.bookstore.BookList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.bookstore.R;

public class BooksList extends RecyclerView.ViewHolder {

    protected TextView title, author, publisher, price, stock, url, total;
    protected ImageButton plus, minus;
    protected RelativeLayout blueprint;

    public BooksList(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.author = (TextView) itemView.findViewById(R.id.author);
        this.publisher = (TextView) itemView.findViewById(R.id.publisher);
        this.price = (TextView) itemView.findViewById(R.id.price);
        this.stock = (TextView) itemView.findViewById(R.id.stock);
        this.url = (TextView) itemView.findViewById(R.id.url);
        this.total = (TextView) itemView.findViewById(R.id.total);
        blueprint = (RelativeLayout) itemView.findViewById(R.id.blueprint);
        this.plus = (ImageButton) itemView.findViewById(R.id.add);
        this.minus = (ImageButton) itemView.findViewById(R.id.minus);
        itemView.setClickable(true);
    }
}
