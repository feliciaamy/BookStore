package com.example.user.bookstore.BookDetails;

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
 * Created by user on 29/11/16.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentList> {
    private List<CommentRow> commentRowList;
    private Context context;
    private int focusedItem = 0;

    public CommentsAdapter(Context context, List<CommentRow> commentRowList) {
        this.context = context;
        this.commentRowList = commentRowList;
    }

    @Override
    public CommentList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, null);
        CommentList holder = new CommentList(v);

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
    public void onBindViewHolder(CommentList holder, int position) {
        CommentRow comment = commentRowList.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        holder.name.setText(comment.getName() + "   " + comment.getUsefulness() + "/5.0");
        holder.date.setText(comment.getDate());
        holder.comment.setText(comment.getComment());
        holder.rate.setRating(Float.parseFloat(comment.getRating()));
        holder.url.setText(comment.getUrl());
    }

    @Override
    public int getItemCount() {
        return (null != commentRowList ? commentRowList.size() : 0);
    }

    public void clearAdapter() {
        commentRowList.clear();
        notifyDataSetChanged();
    }
}