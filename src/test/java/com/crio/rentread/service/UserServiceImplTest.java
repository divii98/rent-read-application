package com.crio.rentread.service;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.Role;
import com.crio.rentread.entity.User;
import com.crio.rentread.exception.AlreadyExistException;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.exchange.UserLoginResponse;
import com.crio.rentread.exchange.UserRegisterRequest;
import com.crio.rentread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserRegisterRequest request = new UserRegisterRequest("John", "Doe", "john.doe@mail.com", "password", null);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Act
        String result = userService.registerUser(request);

        // Assert
        assertEquals(Constants.REGISTERED_SUCCESSFULLY, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_AlreadyExists() {
        // Arrange
        UserRegisterRequest request = new UserRegisterRequest("John", "Doe", "john.doe@mail.com", "password", null);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> userService.registerUser(request));
        assertEquals(Constants.ALREADY_EXIST_WITH_SAME_EMAIL, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        String email = "john.doe@mail.com";
        User user = new User(1L, "John", "Doe", email, "encodedPassword", Role.USER);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserLoginResponse response = userService.login();

        // Assert
        assertNotNull(response);
        assertEquals(Constants.USER_SUCCESS_LOGGED_MESSAGE, response.getMessage());
        assertEquals(user, response.getUserDetails());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        String email = "john.doe@mail.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.login());
        assertEquals(Constants.NOT_FOUND_WITH_GIVEN_EMAIL, exception.getMessage());
    }
}
