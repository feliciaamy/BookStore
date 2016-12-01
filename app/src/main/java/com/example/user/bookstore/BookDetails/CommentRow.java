package com.example.user.bookstore.BookDetails;

/**
 * Created by user on 29/11/16.
 */

public class CommentRow {
    private String name, comment, rating, url, date, usefulness;

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUsefulness() {
        return usefulness;
    }

    public void setUsefulness(String usefulness) {
        this.usefulness = usefulness;
    }
}
