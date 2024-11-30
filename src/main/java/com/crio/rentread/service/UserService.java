package com.crio.rentread.service;

import com.crio.rentread.entity.User;
import com.crio.rentread.exchange.UserLoginResponse;
import com.crio.rentread.exchange.UserRegisterRequest;

public interface UserService {
    String registerUser(UserRegisterRequest requestBody);

    UserLoginResponse login();
}
