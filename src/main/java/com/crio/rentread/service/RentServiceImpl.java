package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Book;
import com.crio.rentread.entity.Rent;
import com.crio.rentread.entity.Status;
import com.crio.rentread.entity.User;
import com.crio.rentread.exception.BookAlreadyRentedException;
import com.crio.rentread.exception.MaxAllowedActiveRentalException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.repository.BookRepository;
import com.crio.rentread.repository.RentRepository;
import com.crio.rentread.repository.UserRepository;
import com.crio.rentread.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    RentRepository rentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @Autowired
    CustomUserDetailService detailService;

    @Transactional
    @Override
    public String rentBook(Long id) {
        Book book = getBook(id);
        if (!book.isAvailable())
            throw new BookAlreadyRentedException("Requested book is already rented and not available");

        User user = getUser();
        long bookCountRentedByUser = rentRepository.countByUserAndStatus(user, Status.ACTIVE);
        if (bookCountRentedByUser >= Constants.MAX_ACTIVE_BOOK_RENT_ALLOWED)
            throw new MaxAllowedActiveRentalException("Cannot rent more than " + Constants.MAX_ACTIVE_BOOK_RENT_ALLOWED + " books at a time");

        Rent rent = new Rent();
        rent.setBook(book);
        rent.setUser(user);
        rentRepository.save(rent);

        book.setAvailable(false);
        bookRepository.save(book);
        return "Book rented successfully for the user";
    }


    @Override
    @Transactional
    public String returnBook(Long id) {
        Book book = getBook(id);
        User user = getUser();
        Rent rent = rentRepository.findByUserAndBook(user,book)
                .orElseThrow(()->new NotFoundException("Given book ID is not rented by user"));
        if(rent.getStatus().equals(Status.RETURNED))
                return "Book already returned by the user";

        book.setAvailable(true);
        bookRepository.save(book);

        rent.setStatus(Status.RETURNED);
        rentRepository.save(rent);
        return "Book returned successfully for the user";
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND_WITH_GIVEN_ID));
    }

    private User getUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(Constants.NOT_FOUND_WITH_GIVEN_EMAIL));
    }


}
