package com.example.user.bookstore.BookDetails;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 29/11/16.
 */

public class CommentRow {
    int rating;
    Usefulness usefulness;
    Map<Usefulness, Integer> usefulness_total = new HashMap<Usefulness, Integer>();
    private String name, comment, url, date;

    public Map<Usefulness, Integer> getUsefulness_total() {
        return usefulness_total;
    }

    public void setUsefulness_total(Map<Usefulness, Integer> usefulness_total) {
        this.usefulness_total = usefulness_total;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;

    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Usefulness getUsefulness() {
        return usefulness;
    }

    public void setUsefulness(Usefulness usefulness) {
        this.usefulness = usefulness;
    }

    public void setUsefulnessFor(Usefulness usefulness, int value) {
        usefulness_total.put(usefulness, value);
        Log.d("DEBUG", usefulness_total.toString());
    }

    public int getUsefulnessFor(Usefulness usefulness) {
        return usefulness_total.get(usefulness);
    }
}
