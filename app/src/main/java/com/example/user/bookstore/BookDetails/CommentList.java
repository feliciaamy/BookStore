package com.example.user.bookstore.BookDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.bookstore.R;

import java.util.HashMap;
import java.util.Map;

public class CommentList extends RecyclerView.ViewHolder {

    protected TextView name, comment, url, date, like, soso, unlike;
    protected RatingBar rate;
    protected RelativeLayout blueprint;
    protected ImageButton like_button;
    protected ImageButton unlike_button;
    protected ImageButton soso_button;
    protected Map<Usefulness, Integer> drawableMap = new HashMap<>();
    protected Map<Usefulness, ImageButton> buttonMap = new HashMap<>();
    protected Map<Usefulness, TextView> labelMap = new HashMap<>();

    public CommentList(View itemView) {
        super(itemView);
        this.soso = (TextView) itemView.findViewById(R.id.soso);
        this.like = (TextView) itemView.findViewById(R.id.like);
        this.unlike = (TextView) itemView.findViewById(R.id.unlike);
        this.like_button = (ImageButton) itemView.findViewById(R.id.likeButton);
        this.unlike_button = (ImageButton) itemView.findViewById(R.id.unlikeButton);
        this.soso_button = (ImageButton) itemView.findViewById(R.id.sosoButton);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.date = (TextView) itemView.findViewById(R.id.date);
        this.comment = (TextView) itemView.findViewById(R.id.comment);
        this.rate = (RatingBar) itemView.findViewById(R.id.rate_comment);
        this.url = (TextView) itemView.findViewById(R.id.url);
        blueprint = (RelativeLayout) itemView.findViewById(R.id.comment_blueprint);
        itemView.setClickable(true);
        buttonMap.put(Usefulness.LIKE, like_button);
        buttonMap.put(Usefulness.UNLIKE, unlike_button);
        buttonMap.put(Usefulness.NEUTRAL, soso_button);
        drawableMap.put(Usefulness.LIKE, R.drawable.like);
        drawableMap.put(Usefulness.UNLIKE, R.drawable.ic_unlike);
        drawableMap.put(Usefulness.NEUTRAL, R.drawable.ic_soso);
        labelMap.put(Usefulness.LIKE, like);
        labelMap.put(Usefulness.UNLIKE, unlike);
        labelMap.put(Usefulness.NEUTRAL, soso);
    }

    public ImageButton getButton(Usefulness usefulness) {
        return buttonMap.get(usefulness);
    }

    public int getDrawable(Usefulness usefulness) {
        return drawableMap.get(usefulness);
    }

    public TextView getLabel(Usefulness usefulness) {
        return labelMap.get(usefulness);
    }
}
