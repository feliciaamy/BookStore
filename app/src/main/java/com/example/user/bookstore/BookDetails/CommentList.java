package com.example.user.bookstore.BookDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.bookstore.R;

public class CommentList extends RecyclerView.ViewHolder {

    protected TextView name, comment, url, date;
    protected RatingBar rate;
    protected RelativeLayout blueprint;

    public CommentList(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.date = (TextView) itemView.findViewById(R.id.date);
        this.comment = (TextView) itemView.findViewById(R.id.comment);
        this.rate = (RatingBar) itemView.findViewById(R.id.rate_comment);
        this.url = (TextView) itemView.findViewById(R.id.url);
        blueprint = (RelativeLayout) itemView.findViewById(R.id.comment_blueprint);
        itemView.setClickable(true);
    }
}
