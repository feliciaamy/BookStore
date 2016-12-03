package com.example.user.bookstore.BookList;

/**
 * Created by user on 23/11/16.
 */

public class BookRow {
    private String title = "", author = "", publisher = "", url = "";
    private int total = 0, stock = 0;
    private double price = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPublisher() {
        return publisher;

    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUrl() {
        return url;

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return title + "; " + url + "; " + author + "; " + publisher + "; " + stock + "; " + price + "; " + total;
    }

    @Override
    public int hashCode() {
        return 31 * url.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookRow bookRow = (BookRow) o;

        return url.equals(bookRow.url);
    }
}
