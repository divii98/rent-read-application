package com.crio.rentread.controller;

import com.crio.rentread.exchange.UserLoginResponse;
import com.crio.rentread.exchange.UserRegisterRequest;
import com.crio.rentread.repository.UserRepository;
import com.crio.rentread.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserController {
    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public String registerUserController(@Valid @RequestBody UserRegisterRequest requestBody) {
        log.info("Register Api called for user: " + requestBody.getFirst_name());
        return userService.registerUser(requestBody);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/login")
    public UserLoginResponse loginUserController(){
        log.info("Login request called for user");
        return userService.login();
    }
}
