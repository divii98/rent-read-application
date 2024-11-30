package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Book;
import com.crio.rentread.exception.AlreadyExistException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.exchange.BookAddRequest;
import com.crio.rentread.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book addBook(BookAddRequest requestBody) {
        if (bookRepository.existsByTitle(requestBody.getTitle()))
            throw new AlreadyExistException(Constants.BOOK_EXIST_WITH_SAME_NAME);
        Book book = new Book();
        mapBook(requestBody, book);
        return bookRepository.save(book);
    }

    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Long id, BookAddRequest requestBody) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID));
        mapBook(requestBody, book);
        return bookRepository.saveAndFlush(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id))
            throw new NotFoundException(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID);
        bookRepository.deleteById(id);
    }

    private void mapBook(BookAddRequest requestBody, Book book) {
        if (requestBody.getTitle() != null)
            book.setTitle(requestBody.getTitle());
        if (requestBody.getAuthor() != null)
            book.setAuthor(requestBody.getAuthor());
        if (requestBody.getGenre() != null)
            book.setGenre(requestBody.getGenre());
    }
}
