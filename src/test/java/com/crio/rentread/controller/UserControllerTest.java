package com.crio.rentread.controller;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Role;
import com.crio.rentread.entity.User;
import com.crio.rentread.exchange.UserLoginResponse;
import com.crio.rentread.exchange.UserRegisterRequest;
import com.crio.rentread.service.UserService;
import com.crio.rentread.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testRegisterUser_Success() throws Exception {
        // Mock the service layer response
        when(userService.registerUser(any(UserRegisterRequest.class)))
                .thenReturn(Constants.REGISTERED_SUCCESSFULLY);

        // Define the request payload
        String requestBody = """
                {
                    "first_name": "John",
                    "last_name": "Doe",
                    "email": "john.doe@example.com",
                    "password": "securepassword"
                }
                """;

        // Perform POST request and validate response
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(Constants.REGISTERED_SUCCESSFULLY));
    }

    @Test
    void testRegisterUser_InvalidRequest() throws Exception {
        // Define an invalid request payload (missing "email")
        String requestBody = """
                {
                    "first_name": "John",
                    "last_name": "Doe",
                    "password": "securepassword"
                }
                """;

        // Perform POST request and expect validation failure
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Error").value("email cannot be blank")); // Custom error handling
    }

    @WithMockUser(username = "mail@mail.com", roles = "USER")
    @Test
    void testLoginUser_Success() throws Exception {
        User user = new User(1L, "John", "Doe", "mail@mail.com", "abc", Role.USER);
        // Mock the service layer response
        UserLoginResponse response = new UserLoginResponse(Constants.USER_SUCCESS_LOGGED_MESSAGE, user);
        when(userService.login()).thenReturn(response);

        // Perform POST request and validate response
        mockMvc.perform(post("/user/login"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(Constants.USER_SUCCESS_LOGGED_MESSAGE))
                .andExpect(jsonPath("$.userDetails.firstName").value("John"));
    }
}
