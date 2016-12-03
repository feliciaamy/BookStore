package com.example.user.bookstore.BookDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bookstore.Database.Action;
import com.example.user.bookstore.Database.Database;
import com.example.user.bookstore.Login;
import com.example.user.bookstore.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 29/11/16.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentList> {
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private List<CommentRow> commentRowList;
    private Context context;
    private int focusedItem = 0;

    public CommentsAdapter(Context context, List<CommentRow> commentRowList) {
        this.context = context;
        this.commentRowList = commentRowList;
    }

    @Override
    public CommentList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentcard, null);
        final CommentList holder = new CommentList(v);

//        TODO: CREATE A FUNCTION FOR THIS
        holder.like_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CommentRow comment = commentRowList.get(holder.getLayoutPosition());
                Usefulness newUsefulness = Usefulness.LIKE;
                Usefulness oldUsefulness = comment.getUsefulness();
                boolean same = oldUsefulness == newUsefulness;
                int value = same ? 3 : 1;
                int prevRate = comment.getUsefulnessFor(newUsefulness);
                int newUsefulnessRate = same ? prevRate - 1 : prevRate + 1;
                if (oldUsefulness != Usefulness.NONE) {
                    int oldUsefulnessRate = comment.getUsefulnessFor(oldUsefulness) - 1;
                    comment.setUsefulnessFor(oldUsefulness, oldUsefulnessRate);
                    int drawable = holder.getDrawable(oldUsefulness);
                    holder.getButton(oldUsefulness).setImageResource(drawable);
                    holder.getLabel(oldUsefulness).setText(oldUsefulnessRate + "");
                }
                updateUsefulness(value, oldUsefulness, comment.getUrl(), comment.getName());
                comment.setUsefulness(same ? Usefulness.NONE : newUsefulness);
                if (!same) {
                    comment.setUsefulnessFor(newUsefulness, newUsefulnessRate);
                    holder.like.setText(newUsefulnessRate + "");
                    holder.like_button.setImageResource(R.drawable.ic_likechosen);
                }
            }
        });
        holder.unlike_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CommentRow comment = commentRowList.get(holder.getLayoutPosition());
                Usefulness newUsefulness = Usefulness.UNLIKE;
                Usefulness oldUsefulness = comment.getUsefulness();
                boolean same = oldUsefulness == newUsefulness;
                int value = same ? 3 : -1;
                int prevRate = comment.getUsefulnessFor(newUsefulness);
                int newUsefulnessRate = same ? prevRate - 1 : prevRate + 1;
                if (oldUsefulness != Usefulness.NONE) {
                    int oldUsefulnessRate = comment.getUsefulnessFor(oldUsefulness) - 1;
                    comment.setUsefulnessFor(oldUsefulness, oldUsefulnessRate);
                    int drawable = holder.getDrawable(oldUsefulness);
                    holder.getButton(oldUsefulness).setImageResource(drawable);
                    holder.getLabel(oldUsefulness).setText(oldUsefulnessRate + "");
                }
                updateUsefulness(value, oldUsefulness, comment.getUrl(), comment.getName());
                comment.setUsefulness(same ? Usefulness.NONE : newUsefulness);
                if (!same) {
                    comment.setUsefulnessFor(newUsefulness, newUsefulnessRate);
                    holder.unlike.setText(newUsefulnessRate + "");
                    holder.unlike_button.setImageResource(R.drawable.ic_unlikechosen);
                }
            }
        });
        holder.soso_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CommentRow comment = commentRowList.get(holder.getLayoutPosition());
                Usefulness newUsefulness = Usefulness.NEUTRAL;
                Usefulness oldUsefulness = comment.getUsefulness();
                boolean same = oldUsefulness == newUsefulness;
                int value = same ? 3 : 0;
                int prevRate = comment.getUsefulnessFor(newUsefulness);
                int newUsefulnessRate = same ? prevRate - 1 : prevRate + 1;
                if (oldUsefulness != Usefulness.NONE) {
                    int oldUsefulnessRate = comment.getUsefulnessFor(oldUsefulness) - 1;
                    comment.setUsefulnessFor(oldUsefulness, oldUsefulnessRate);
                    int drawable = holder.getDrawable(oldUsefulness);
                    holder.getButton(oldUsefulness).setImageResource(drawable);
                    holder.getLabel(oldUsefulness).setText(oldUsefulnessRate + "");
                }
                updateUsefulness(value, oldUsefulness, comment.getUrl(), comment.getName());
                comment.setUsefulness(same ? Usefulness.NONE : newUsefulness);
                if (!same) {
                    comment.setUsefulnessFor(newUsefulness, newUsefulnessRate);
                    holder.soso.setText(newUsefulnessRate + "");
                    holder.soso_button.setImageResource(R.drawable.ic_sosochosen);
                }
            }
        });
        return holder;
    }

    private void updateUsefulness(int value, Usefulness oldUsefulness, String isbn13, String feedback_user) {
        Database database = new Database();
        Action action;
        if (oldUsefulness == Usefulness.NONE) {
            action = Action.INSERTUSEFULNESS;
        } else if (value == 3) {
            action = Action.DELETEUSEFULNESS;
        } else {
            action = Action.UPDATEUSEFULNESS;
        }

        String result = null;
        try {
            result = database.execute(action.toString(), isbn13, feedback_user, Login.USERNAME, value + "").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("COMMENT DATABASE", action.toString() + ", " + isbn13 + ", " + feedback_user + ", " + Login.USERNAME + ", " + value + " = " + result);
    }

    @Override
    public void onBindViewHolder(CommentList holder, int position) {
        CommentRow comment = commentRowList.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        holder.name.setText(comment.getName());
        holder.date.setText(comment.getDate());
        holder.comment.setText(comment.getComment());
        holder.rate.setRating(comment.getRating());
        holder.url.setText(comment.getUrl());
        //TODO: FIX THIS LATER
        holder.unlike.setText(comment.getUsefulnessFor(Usefulness.UNLIKE) + "");
        holder.soso.setText(comment.getUsefulnessFor(Usefulness.NEUTRAL) + "");
        holder.like.setText(comment.getUsefulnessFor(Usefulness.LIKE) + "");

        holder.unlike_button.setImageResource(R.drawable.ic_unlike);
        holder.soso_button.setImageResource(R.drawable.ic_soso);
        holder.like_button.setImageResource(R.drawable.like);
        switch (comment.getUsefulness()) {
            case UNLIKE:
                holder.unlike_button.setImageResource(R.drawable.ic_unlikechosen);
                break;
            case LIKE:
                holder.like_button.setImageResource(R.drawable.ic_likechosen);
                break;
            case NEUTRAL:
                holder.soso_button.setImageResource(R.drawable.ic_sosochosen);
        }
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