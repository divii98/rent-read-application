package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Role;
import com.crio.rentread.entity.User;
import com.crio.rentread.exception.AlreadyExistException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.exchange.UserLoginResponse;
import com.crio.rentread.exchange.UserRegisterRequest;
import com.crio.rentread.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserRegisterRequest requestBody) {
        if (userRepository.existsByEmail(requestBody.getEmail()))
            throw new AlreadyExistException(Constants.ALREADY_EXIST_WITH_SAME_EMAIL);
        User user = new User();
        user.setFirstName(requestBody.getFirst_name());
        user.setLastName(requestBody.getLast_name());
        user.setEmail(requestBody.getEmail());
        user.setPassword(passwordEncoder.encode(requestBody.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return Constants.REGISTERED_SUCCESSFULLY;
    }

    @Override
    public UserLoginResponse login() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException(Constants.NOT_FOUND_WITH_GIVEN_EMAIL));
        return new UserLoginResponse(Constants.USER_SUCCESS_LOGGED_MESSAGE,user);
    }
}
