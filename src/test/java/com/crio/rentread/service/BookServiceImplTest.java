package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Book;
import com.crio.rentread.exception.AlreadyExistException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.exchange.BookAddRequest;
import com.crio.rentread.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook_Success() {
        // Arrange
        BookAddRequest request = new BookAddRequest("Title", "Author", "Genre");
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(false);
        Book savedBook = new Book(1L, "Title", "Author", "Genre",true);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = bookService.addBook(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedBook.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBook_AlreadyExists() {
        // Arrange
        BookAddRequest request = new BookAddRequest("Title", "Author", "Genre");
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(true);

        // Act & Assert
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> bookService.addBook(request));
        assertEquals(Constants.BOOK_EXIST_WITH_SAME_NAME, exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetBook_Success() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(1L,"Title", "Author", "Genre",true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.getBook(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    void testGetBook_NotFound() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.getBook(bookId));
        assertEquals(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID, exception.getMessage());
    }

    @Test
    void testGetAllBooks_Success() {
        // Arrange
        List<Book> books = Arrays.asList(
                new Book(1L, "Title1", "Author1", "Genre1",true),
                new Book(2L, "Title2", "Author2", "Genre2",true)
        );
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateBook_Success() {
        // Arrange
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "OldTitle", "OldAuthor", "OldGenre",true);
        BookAddRequest request = new BookAddRequest("NewTitle", "NewAuthor", "NewGenre");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(existingBook);

        // Act
        Book result = bookService.updateBook(bookId, request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        // Arrange
        Long bookId = 1L;
        BookAddRequest request = new BookAddRequest("NewTitle", "NewAuthor", "NewGenre");
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.updateBook(bookId, request));
        assertEquals(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID, exception.getMessage());
        verify(bookRepository, never()).saveAndFlush(any(Book.class));
    }

    @Test
    void testDeleteBook_Success() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_NotFound() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.deleteBook(bookId));
        assertEquals(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID, exception.getMessage());
        verify(bookRepository, never()).deleteById(bookId);
    }
}
