package com.crio.rentread.controller;

import com.crio.rentread.service.RentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@Log4j2
public class RentController {

    @Autowired
    RentService rentService;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{id}/rent")
    public String rentBookController(@PathVariable Long id){
        log.info("Book rent endpoint called for book id: "+id);
        return rentService.rentBook(id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{id}/return")
    public String returnBookController(@PathVariable Long id){
        log.info("Book return request called for ID: "+id);
        return rentService.returnBook(id);
    }
}
