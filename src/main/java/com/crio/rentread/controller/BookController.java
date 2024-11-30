package com.crio.rentread.controller;

import com.crio.rentread.entity.Book;
import com.crio.rentread.exchange.BookAddRequest;
import com.crio.rentread.service.BookService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Book addBookController(@Valid @RequestBody BookAddRequest requestBody) {
        log.info("Book add request called with body: " + requestBody);
        return bookService.addBook(requestBody);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookByIdController(@PathVariable Long id) {
        log.info("Book get request for Id: " + id);
        return bookService.getBook(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getAllBookController() {
        log.info("Book get all request called");
        return bookService.getAllBooks();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Book updateBookById(@RequestBody BookAddRequest request,@PathVariable Long id) {
        log.info("Book update called for id: " + id);
        return bookService.updateBook(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookByIdController(@PathVariable Long id) {
        log.info("Book get all request called");
        bookService.deleteBook(id);
    }
}
