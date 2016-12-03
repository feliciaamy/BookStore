package com.example.user.bookstore.PlaceOrder;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.user.bookstore.BookList.BookRow;
import com.example.user.bookstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2/12/16.
 */

public class PlaceOrderAdapter extends ArrayAdapter<BookRow> {
    private static final String EXTRA_LIST = "shoppingBag";
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private Map<BookRow, Integer> shoppingBag;
    private List<BookRow> shoppingList;
    private Context context;
    private LayoutInflater mInflater;
    private double totalPayment = 0;

    public PlaceOrderAdapter(Context context, Map<BookRow, Integer> shoppingBag) {
        super(context, 0, new ArrayList<>(shoppingBag.keySet()));
        this.context = context;
        this.shoppingBag = shoppingBag;
        shoppingList = new ArrayList<>(shoppingBag.keySet());
        shoppingList.add(new BookRow());
        Log.d("ShoppingList", shoppingList.toString());
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("TEST", getItem(0).toString());
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public BookRow getItem(int i) {
        return shoppingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        OrderList holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.orderlist, null);
            holder = new OrderList(view);
            BookRow book = shoppingList.get(position);
            if (book.getUrl().equals("")) {
                holder.title.setText("TOTAL");
                holder.title.setTextSize(16);
                holder.title.setTypeface(null, Typeface.BOLD_ITALIC);
                holder.total.setText("");
                holder.price.setText(df2.format(totalPayment) + " USD");
                holder.price.setTextSize(20);

            } else {
                holder.title.setText(book.getTitle());
                double totalPrice = book.getPrice() * shoppingBag.get(book);
                totalPayment += totalPrice;
                holder.price.setText(df2.format(totalPrice) + " USD");
                holder.total.setText(shoppingBag.get(book) + " x " + book.getPrice() + " USD");
                view.setTag(holder);
                Log.d("Debug", "add " + book.toString());
            }

        } else {
            holder = (OrderList) view.getTag();
        }

        return view;
    }
}
