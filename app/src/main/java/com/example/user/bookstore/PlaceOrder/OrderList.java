package com.example.user.bookstore.PlaceOrder;

import android.view.View;
import android.widget.TextView;

import com.example.user.bookstore.R;

/**
 * Created by user on 2/12/16.
 */

public class OrderList {
    TextView title, total, price;

    public OrderList(View itemView) {
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.total = (TextView) itemView.findViewById(R.id.total);
        this.price = (TextView) itemView.findViewById(R.id.price);
    }
}
