package com.example.user.bookstore.BookDetails;

/**
 * Created by user on 3/12/16.
 */

public enum Usefulness {
    UNLIKE(0),
    NEUTRAL(1),
    LIKE(2),
    NONE(-1);

    private final int value;

    Usefulness(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
