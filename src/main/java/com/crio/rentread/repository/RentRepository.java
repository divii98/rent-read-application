package com.crio.rentread.repository;

import com.crio.rentread.entity.Book;
import com.crio.rentread.entity.Rent;
import com.crio.rentread.entity.Status;
import com.crio.rentread.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent,Long> {
    long countByUserAndStatus(User user, Status status);
    Optional<Rent> findByUserAndBook(User user, Book book);
}
