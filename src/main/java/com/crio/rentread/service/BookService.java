package com.crio.rentread.service;

import com.crio.rentread.exchange.BookAddRequest;
import com.crio.rentread.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(BookAddRequest requestBody);

    Book getBook(Long id);

    List<Book> getAllBooks();

    Book updateBook(Long id, BookAddRequest requestBody);

    void deleteBook(Long id);
}
