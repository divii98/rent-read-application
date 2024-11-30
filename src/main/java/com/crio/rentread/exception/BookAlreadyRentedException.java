package com.crio.rentread.exception;

public class BookAlreadyRentedException extends RuntimeException {
    public BookAlreadyRentedException(String s) {
        super(s);
    }
}
