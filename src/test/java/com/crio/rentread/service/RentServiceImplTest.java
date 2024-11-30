package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.*;
import com.crio.rentread.exception.BookAlreadyRentedException;
import com.crio.rentread.exception.MaxAllowedActiveRentalException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.repository.BookRepository;
import com.crio.rentread.repository.RentRepository;
import com.crio.rentread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentServiceImplTest {

    @Mock
    private RentRepository rentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private RentServiceImpl rentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testRentBook_Success() {
        // Arrange
        String email = "john.doe@mail.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",true);
        book.setAvailable(true);
        User user = new User(1L, "John", "Doe", "email@example.com", "password", null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentRepository.countByUserAndStatus(user, Status.ACTIVE)).thenReturn(0L);

        // Act
        String result = rentService.rentBook(bookId);

        // Assert
        assertEquals("Book rented successfully for the user", result);
        assertFalse(book.isAvailable());
        verify(bookRepository, times(1)).save(book);
        verify(rentRepository, times(1)).save(any(Rent.class));
    }

    @Test
    void testRentBook_BookAlreadyRented() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",false);
        book.setAvailable(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act & Assert
        BookAlreadyRentedException exception = assertThrows(BookAlreadyRentedException.class, () -> rentService.rentBook(bookId));
        assertEquals("Requested book is already rented and not available", exception.getMessage());
    }

    @Test
    void testRentBook_MaxAllowedActiveRentalException() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",true);
        book.setAvailable(true);
        User user = new User(1L, "John", "Doe", "email@example.com", "password", null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentRepository.countByUserAndStatus(user, Status.ACTIVE)).thenReturn((long) Constants.MAX_ACTIVE_BOOK_RENT_ALLOWED);

        // Act & Assert
        MaxAllowedActiveRentalException exception = assertThrows(MaxAllowedActiveRentalException.class, () -> rentService.rentBook(bookId));
        assertEquals("Cannot rent more than " + Constants.MAX_ACTIVE_BOOK_RENT_ALLOWED + " books at a time", exception.getMessage());
    }

    @Test
    void testReturnBook_Success() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",true);
        book.setAvailable(false);
        User user = new User(1L, "John", "Doe", "email@example.com", "password", null);
        Rent rent = new Rent();
        rent.setBook(book);
        rent.setUser(user);
        rent.setStatus(Status.ACTIVE);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(rent));

        // Act
        String result = rentService.returnBook(bookId);

        // Assert
        assertEquals("Book returned successfully for the user", result);
        assertTrue(book.isAvailable());
        assertEquals(Status.RETURNED, rent.getStatus());
        verify(bookRepository, times(1)).save(book);
        verify(rentRepository, times(1)).save(rent);
    }

    @Test
    void testReturnBook_NotRented() {
        // Arrange
        String email = "email@example.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",true);
        User user = new User(1L, "John", "Doe", "email@example.com", "password", Role.USER);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> rentService.returnBook(bookId));
//        assertEquals("Given book ID is not rented by user", exception.getMessage());
    }

    @Test
    void testReturnBook_AlreadyReturned() {
        // Arrange
        String email = "john.doe@mail.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "Genre",true);
        User user = new User(1L, "John", "Doe", "john.doe@mail.com", "password", null);
        Rent rent = new Rent();
        rent.setBook(book);
        rent.setUser(user);
        rent.setStatus(Status.RETURNED);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(rent));

        // Act
        String result = rentService.returnBook(bookId);

        // Assert
        assertEquals("Book already returned by the user", result);
        verify(bookRepository, never()).save(book);
        verify(rentRepository, never()).save(rent);
    }
}
